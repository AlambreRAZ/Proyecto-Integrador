package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioDao;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Si intenta acceder a /login por GET, lo mandamos al jsp
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String credencial = request.getParameter("email");
        String contra = request.getParameter("contra");

        if (credencial == null || credencial.trim().isEmpty() || contra == null || contra.trim().isEmpty()) {
            request.setAttribute("error", "Por favor ingresa tus credenciales.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        Usuario usuario = usuarioDao.login(credencial, contra);

        if (usuario != null) {
            // Usuario autenticado correctamente
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario); // Filtro requiere esto
            
            // Redirección según rol
            String rol = usuario.getRol();
            if ("desarrollo".equalsIgnoreCase(rol)) {
                response.sendRedirect(request.getContextPath() + "/vista_general_desarrollador_de.jsp");
            } else if ("coordinador".equalsIgnoreCase(rol)) {
                response.sendRedirect(request.getContextPath() + "/vista_general_coordinador_co.jsp");
            } else if ("docente".equalsIgnoreCase(rol)) {
                response.sendRedirect(request.getContextPath() + "/vista_general_docente_do.jsp");
            } else {
                // Rol desconocido (no debería pasar por el CHECK de la BD)
                request.setAttribute("error", "El rol de este usuario no es válido.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            // Credenciales incorrectas o usuario inactivo
            request.setAttribute("error", "Correo/Número de empleado o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
