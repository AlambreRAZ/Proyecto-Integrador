package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioListaDao;

import java.io.IOException;

@WebServlet("/EliminarPeriodoServlet")
public class EliminarPeriodoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int idPeriodo = Integer.parseInt(idStr);

                UsuarioListaDao dao = new UsuarioListaDao();
                boolean eliminado = dao.eliminarPeriodo(idPeriodo);

                if (eliminado) {
                    System.out.println("Periodo " + idPeriodo + " eliminado correctamente.");
                } else {
                    System.err.println("No se pudo eliminar el periodo " + idPeriodo);
                }

            } catch (NumberFormatException e) {
                System.err.println("ID de periodo inválido: " + idStr);
            }
        }

        // Redirecciona de vuelta a la página principal de gestión
        response.sendRedirect("gestion_periodos_carga_de.jsp");
    }
}