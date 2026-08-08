package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mx.edu.utez.DesarrolloAcademico.model.Periodo;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioListaDao;

import java.io.IOException;
import java.util.List;

@WebServlet("/GestionPeriodosServlet")
public class GestionPeriodosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioListaDao dao = new UsuarioListaDao();

        // 1. Obtener los datos desde el DAO
        List<Periodo> lista = dao.obtenerTodosLosPeriodos();

        // 2. Guardar la lista en el request para el JSP
        request.setAttribute("listaPeriodos", lista);

        // 3. Redirigir hacia la vista JSP mediante forward
        request.getRequestDispatcher("gestion_periodos_carga_de.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}