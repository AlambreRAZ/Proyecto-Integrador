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

@WebServlet(name = "CambiarEstadoUsuarioServlet", value = "/CambiarEstadoUsuarioServlet")
public class CambiarEstadoUsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            out.write("{\"success\": false, \"message\": \"Sesión inválida o expirada.\"}");
            return;
        }

        Usuario admin = (Usuario) session.getAttribute("usuario");
        if (!"desarrollo".equalsIgnoreCase(admin.getRol()) && !"coordinador".equalsIgnoreCase(admin.getRol())) {
            out.write("{\"success\": false, \"message\": \"No tienes permisos para esta acción.\"}");
            return;
        }

        try {
            int idUsuario = Integer.parseInt(request.getParameter("id"));
            int nuevoEstado = Integer.parseInt(request.getParameter("estado"));

            // Prevenir desactivarse a uno mismo
            if (idUsuario == admin.getIdUsuario() && nuevoEstado == 0) {
                out.write("{\"success\": false, \"message\": \"No puedes desactivar tu propia cuenta.\"}");
                return;
            }

            UsuarioDao dao = new UsuarioDao();
            boolean exito = dao.cambiarEstado(idUsuario, nuevoEstado);

            if (exito) {
                out.write("{\"success\": true, \"message\": \"Estado actualizado correctamente.\"}");
            } else {
                out.write("{\"success\": false, \"message\": \"No se encontró el usuario o no se pudo cambiar el estado.\"}");
            }

        } catch (NumberFormatException e) {
            out.write("{\"success\": false, \"message\": \"Parámetros inválidos.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Error del servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}
