package mx.edu.utez.DesarrolloAcademico.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class FiltroAutenticacion extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);

        boolean loginRequest =
                requestURI.endsWith("login.jsp") ||
                        requestURI.endsWith("/login") ||
                        requestURI.endsWith("registro.jsp") ||
                        requestURI.endsWith("/register") ||
                        requestURI.endsWith("recuperar-contra.jsp") ||
                        requestURI.endsWith("/recuperar") ||
                        requestURI.endsWith("/reset") ||
                        requestURI.endsWith("vista_general_coordinador_co.jsp") ||
                        requestURI.endsWith("gestion_evento_co.jsp") ||
                        requestURI.endsWith("agregar_docente_co.jsp") ||
                        requestURI.endsWith("agregar_evento_co.jsp") ||
                        requestURI.endsWith("archivo_subido_co.jsp") ||
                        requestURI.endsWith("cargar_archivo_co.jsp") ||
                        requestURI.endsWith("editar_docente_co.jsp") ||
                        requestURI.endsWith("editar_evento_co.jsp") ||
                        requestURI.endsWith("gestion_docente_co.jsp") ||
                        requestURI.endsWith("historial_evento_co.jsp") ||
                        requestURI.endsWith("mi_cuenta_co.jsp") ||
                        requestURI.endsWith("mi_evento_co.jsp") ||
                        requestURI.endsWith("ver_mas_evento_co.jsp") ||
                        requestURI.endsWith("vista_general_docente_do.jsp") ||
                        requestURI.endsWith("mis_eventos_do.jsp") ||
                        requestURI.endsWith("historial_evento_do.jsp") ||
                        requestURI.endsWith("cargar_archivo_do.jsp") ||
                        requestURI.endsWith("archivo_subido_do.jsp") ||
                        requestURI.endsWith("mi_cuenta_do.jsp") ||
                        requestURI.endsWith("ver_mas_evento_do.jsp") ||
                        requestURI.endsWith("vista_general_desarrollador_de.jsp") ||
                        requestURI.endsWith("mi_cuenta_de.jsp") ||
                        requestURI.endsWith("gestion_periodos_carga_de.jsp") ||
                        requestURI.endsWith("agregar_periodos_cargar_de.jsp") ||
                        requestURI.endsWith("editar_periodo_carga_de.jsp") ||
                        requestURI.endsWith("gestion_docente_de.jsp") ||
                        requestURI.endsWith("agregar_docente_de.jsp") ||
                        requestURI.endsWith("editar_docente_de.jsp") ||
                        requestURI.endsWith("gestion_desarrolladores_de.jsp") ||
                        requestURI.endsWith("agregar_desarrollador_de.jsp") ||
                        requestURI.endsWith("editar_desarrollador_de.jsp") ||
                        requestURI.endsWith("gestion_eventos_de.jsp") ||
                        requestURI.endsWith("agregar_evento_de.jsp") ||
                        requestURI.endsWith("editar_evento_de.jsp") ||
                        requestURI.endsWith("ver_mas_evento_de.jsp") ||
                        requestURI.endsWith("historial_eventos_de.jsp") ||
                        requestURI.endsWith("cargar_archivo_de.jsp") ||
                        requestURI.endsWith("archivo_subido_de.jsp")
                ;
        boolean isResource = requestURI.contains("/assets/") || requestURI.contains("/layout/");

        if (loggedIn) {
            if (loginRequest) {
                response.sendRedirect(request.getContextPath() + "/registro.jsp");
            } else {
                chain.doFilter(request, response);
            }
        } else {
            if (loginRequest || isResource) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/registro.jsp");
            }
        }
    }
}