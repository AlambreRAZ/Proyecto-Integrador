package mx.edu.utez.DesarrolloAcademico.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarDesarrollador_Dao;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CambiarEstado", value = "/CambiarEstado")
public class CambiarEstado extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String idStr = request.getParameter("id");
        String estadoStr = request.getParameter("estado");

        try {
            if (idStr == null || estadoStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Faltan datos requeridos (id o estado).\"}");
                return;
            }

            int id = Integer.parseInt(idStr.trim());
            int nuevoEstado = Integer.parseInt(estadoStr.trim());

            AgregarDesarrollador_Dao dao = new AgregarDesarrollador_Dao();
            boolean exito = dao.cambiarEstado(id, nuevoEstado);

            if (exito) {
                String mensaje = nuevoEstado == 1 ? "Docente activado correctamente." : "Docente desactivado correctamente.";
                out.write("{\"success\": true, \"message\": \"" + mensaje + "\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"No se pudo actualizar el estado en la base de datos.\"}");
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\": false, \"message\": \"Formato de parámetros inválido.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Error del servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}