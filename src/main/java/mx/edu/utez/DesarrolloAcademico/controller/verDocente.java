package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarDesarrollador_Dao;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioDao;

import java.io.IOException;

@WebServlet(name = "verDocente", value = "/verDocente")
public class verDocente extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int idUsuario = Integer.parseInt(idStr.trim());

                UsuarioDao dao = new UsuarioDao();

                Usuario docente = dao.obtenerDocentePorId(idUsuario);

                if (docente != null) {
                    request.setAttribute("dev", docente);

                    // Renderizamos la vista de detalles
                    request.getRequestDispatcher("/ver_detalles_docente_de.jsp").forward(request, response);
                    return;
                } else {
                    System.out.println("[verDocente] No se encontró ningún docente con el ID: " + idUsuario);
                }
            } catch (NumberFormatException e) {
                System.err.println("[verDocente] ID con formato inválido: " + idStr);
            }
        }

        // Si el docente no existe o el ID viene vacío, reorientamos a la lista
        response.sendRedirect(request.getContextPath() + "/gestion_docente_de.jsp");
    }
}