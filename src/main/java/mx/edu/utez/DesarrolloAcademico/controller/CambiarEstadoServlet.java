package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioListaDao;

import java.io.IOException;

@WebServlet("/CambiarEstadoServlet")
public class CambiarEstadoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        String estadoStr = request.getParameter("estado");

        if (idStr != null && estadoStr != null) {
            try {
                int idPeriodo = Integer.parseInt(idStr);
                boolean nuevoEstado = Boolean.parseBoolean(estadoStr);

                UsuarioListaDao dao = new UsuarioListaDao();
                dao.cambiarEstadoPeriodo(idPeriodo, nuevoEstado);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}