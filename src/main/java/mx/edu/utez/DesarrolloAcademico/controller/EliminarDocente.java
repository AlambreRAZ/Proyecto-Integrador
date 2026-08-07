package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarDesarrollador_Dao;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioDao;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "EliminarDocente", value = "/EliminarDocente")
@MultipartConfig
public class EliminarDocente extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            String idStr = request.getParameter("idUsuario");

            if (idStr == null || idStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"ID de usuario no recibido.\"}");
                return;
            }

            int idUsuario = Integer.parseInt(idStr);

            // Método en tu DAO que ejecuta DELETE FROM USUARIO WHERE ID_USUARIO = ?
            boolean eliminado = usuarioDao.eliminarUsuario(idUsuario);

            if (eliminado) {
                out.write("{\"success\": true, \"message\": \"El docente fue eliminado permanentemente de la base de datos.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"No se pudo eliminar el registro (puede tener datos asociados).\"}");
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter out = response.getWriter()) {
                out.write("{\"success\": false, \"message\": \"ID inválido.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.write("{\"success\": false, \"message\": \"Error del servidor: " + e.getMessage() + "\"}");
            }
        }
    }
}