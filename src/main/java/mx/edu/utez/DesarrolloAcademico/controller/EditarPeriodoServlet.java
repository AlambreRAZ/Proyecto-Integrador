package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioListaDao;

import java.io.IOException;

@WebServlet("/EditarPeriodoServlet")
public class EditarPeriodoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String idStr = request.getParameter("id");
            String division = request.getParameter("division");
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");

            if (idStr == null || division == null || fechaInicio == null || fechaFin == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Datos incompletos para actualizar.\"}");
                return;
            }

            int id = Integer.parseInt(idStr);
            UsuarioListaDao dao = new UsuarioListaDao();

            // Validar Duplicado en Editar (Excluyendo ID actual)
            if (dao.existeDivision(division, id)) {
                String nombreDivision = dao.obtenerNombreDivision(division);
                response.setStatus(HttpServletResponse.SC_CONFLICT); // Código HTTP 409
                response.getWriter().write("{\"status\":\"duplicate\", \"message\":\"La división " + nombreDivision + " ya tiene un periodo de carga asignado.\"}");
                return;
            }

            boolean actualizado = dao.actualizarPeriodo(id, division, fechaInicio, fechaFin);

            if (actualizado) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"status\":\"success\", \"message\":\"Periodo actualizado correctamente.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"No se pudo actualizar el periodo en la base de datos.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errorClean = e.getMessage() != null ? e.getMessage().replace("\"", "'").replace("\r", "").replace("\n", " ") : "Error interno del servidor";
            response.getWriter().write("{\"status\":\"error\", \"message\":\"" + errorClean + "\"}");
        }
    }
}