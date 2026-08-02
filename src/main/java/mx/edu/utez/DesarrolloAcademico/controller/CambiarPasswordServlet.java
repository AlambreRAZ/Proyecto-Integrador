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
import java.io.PrintWriter;

@WebServlet(name = "CambiarPasswordServlet", value = "/CambiarPasswordServlet")
public class CambiarPasswordServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                out.write("{\"success\": false, \"message\": \"Sesión inválida o expirada.\"}");
                return;
            }
            Usuario u = (Usuario) session.getAttribute("usuario");

            String passActual = request.getParameter("passActual");
            String passNueva = request.getParameter("passNueva");
            String idTargetStr = request.getParameter("idUsuarioTarget");

            if (passActual == null || passActual.isEmpty() || passNueva == null || passNueva.isEmpty()) {
                out.write("{\"success\": false, \"message\": \"Faltan datos de la contraseña.\"}");
                return;
            }

            // Si se especifica idUsuarioTarget (coordinador cambia pass de otro), usar ese ID
            int idUsuarioTarget = u.getIdUsuario();
            if (idTargetStr != null && !idTargetStr.isEmpty()) {
                try { idUsuarioTarget = Integer.parseInt(idTargetStr); } catch (NumberFormatException ignored) {}
            }

            boolean exito = usuarioDao.actualizarPasswordEnCuenta(idUsuarioTarget, passActual, passNueva);

            if (exito) {
                // Actualizar sesión solo si cambió su propia contraseña
                if (idUsuarioTarget == u.getIdUsuario()) {
                    u.setContrasena(passNueva);
                    session.setAttribute("usuario", u);
                }
                out.write("{\"success\": true, \"message\": \"Contraseña actualizada exitosamente.\"}");
            } else {
                out.write("{\"success\": false, \"message\": \"La contraseña actual es incorrecta o hubo un error al actualizar.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Error del servidor: " + e.getMessage() + "\"}");
        }
        
        out.flush();
    }
}
