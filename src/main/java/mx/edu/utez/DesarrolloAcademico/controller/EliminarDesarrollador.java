package mx.edu.utez.DesarrolloAcademico.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarDesarrollador_Dao;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "EliminarDesarrollador", value = "/EliminarDesarrollador")
@MultipartConfig
public class EliminarDesarrollador extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String idStr = request.getParameter("id");
        String estadoStr = request.getParameter("estado");

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\": false, \"message\": \"Id de desarrollador inválido.\"}");
            out.flush();
            return;
        }

        int nuevoEstado = 0; // Por defecto (botón de eliminar), se desactiva.
        if (estadoStr != null && !estadoStr.trim().isEmpty()) {
            try {
                nuevoEstado = Integer.parseInt(estadoStr.trim());
                if (nuevoEstado != 0 && nuevoEstado != 1) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Estado inválido.\"}");
                out.flush();
                return;
            }
        }

        AgregarDesarrollador_Dao dao = new AgregarDesarrollador_Dao();
        boolean exito = dao.cambiarEstado(id, nuevoEstado);

        if (exito) {
            String mensaje = nuevoEstado == 1 ? "Desarrollador activado correctamente." : "Desarrollador desactivado correctamente.";
            out.write("{\"success\": true, \"message\": \"" + mensaje + "\", \"activo\":" + nuevoEstado + "}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"No se pudo actualizar el estado del desarrollador.\"}");
        }
        out.flush();
    }
}