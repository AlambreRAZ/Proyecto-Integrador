package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;

import java.io.IOException;

@WebFilter(urlPatterns = {"*.jsp"})
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();

        // Ignorar login, recursos y otros que no terminan en rol específico
        if (uri.endsWith("login.jsp") || uri.contains("/assets/") || uri.endsWith("recuperar_contrasena.jsp") || uri.endsWith("sidebar_co.jsp") || uri.endsWith("sidebar_de.jsp") || uri.endsWith("sidebar_do.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        // Si la URL es una vista protegida, validamos sesión
        if (uri.endsWith("_de.jsp") || uri.endsWith("_co.jsp") || uri.endsWith("_do.jsp")) {
            HttpSession session = req.getSession(false);
            Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

            if (u == null) {
                res.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }

            String rol = u.getRol() != null ? u.getRol().toLowerCase() : "";

            if (uri.endsWith("_de.jsp") && !rol.equals("desarrollo")) {
                res.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }
            if (uri.endsWith("_co.jsp") && !rol.equals("coordinador")) {
                res.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }
            if (uri.endsWith("_do.jsp") && !rol.equals("docente")) {
                res.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
