package mx.edu.utez.DesarrolloAcademico.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;

import java.io.IOException;

@WebFilter("/*")
public class FiltroAutenticacion extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 1. Prevenir guardado en caché para evitar ver páginas al presionar "Atrás" tras cerrar sesión
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);

        // 2. Definir Páginas y Recursos Públicos (SIN sesión requerida)
        boolean isAuthPage = requestURI.endsWith("login.jsp") ||
                requestURI.endsWith("/login") ||
                requestURI.endsWith("registro.jsp") ||
                requestURI.endsWith("/register") ||
                requestURI.endsWith("recuperar-contra.jsp") ||
                requestURI.endsWith("/recuperar") ||
                requestURI.endsWith("/reset");

        boolean isResource = requestURI.contains("/assets/") ||
                requestURI.contains("/layout/") ||
                requestURI.endsWith(".css") ||
                requestURI.endsWith(".js") ||
                requestURI.endsWith(".png") ||
                requestURI.endsWith(".jpg") ||
                requestURI.endsWith(".ico");

        // 3. Evaluar Estado de Autenticación
        if (loggedIn) {
            if (isAuthPage) {
                // Si el usuario ya está logueado e intenta ir al Login -> Redirigir a su panel
                Usuario user = (Usuario) session.getAttribute("usuario");
                String rol = (user != null && user.getRol() != null) ? user.getRol().toLowerCase() : "";

                switch (rol) {
                    case "desarrollo":
                        response.sendRedirect(request.getContextPath() + "/vista_general_desarrollador_de.jsp");
                        break;
                    case "coordinador":
                        response.sendRedirect(request.getContextPath() + "/vista_general_coordinador_co.jsp");
                        break;
                    case "docente":
                        response.sendRedirect(request.getContextPath() + "/vista_general_docente_do.jsp");
                        break;
                    default:
                        session.invalidate();
                        response.sendRedirect(request.getContextPath() + "/login.jsp");
                        break;
                }
            } else {
                // Usuario autenticado accediendo a cualquier vista privada o Servlet -> Permitir paso
                chain.doFilter(request, response);
            }
        } else {
            if (isAuthPage || isResource) {
                // Usuario no autenticado en páginas de login o recursos -> Permitir paso
                chain.doFilter(request, response);
            } else {
                // Intentando acceder a vistas privadas sin iniciar sesión -> Bloquear
                String requestedWith = request.getHeader("X-Requested-With");
                String acceptHeader = request.getHeader("Accept");

                if ("XMLHttpRequest".equals(requestedWith) || (acceptHeader != null && acceptHeader.contains("application/json"))) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"success\": false, \"message\": \"Sesión expirada. Por favor inicie sesión nuevamente.\"}");
                } else {
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                }
            }
        }
    }
}