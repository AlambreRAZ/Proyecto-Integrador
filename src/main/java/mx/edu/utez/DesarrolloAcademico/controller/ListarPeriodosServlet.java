package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mx.edu.utez.DesarrolloAcademico.model.Periodo;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioListaDao;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/ListarPeriodosServlet")
public class ListarPeriodosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            UsuarioListaDao dao = new UsuarioListaDao();
            List<Periodo> listaPeriodos = dao.obtenerTodosLosPeriodos(); // Método SELECT en tu DAO

            // Construir respuesta JSON manualmente
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < listaPeriodos.size(); i++) {
                Periodo p = listaPeriodos.get(i);
                json.append("{")
                        .append("\"idPeriodo\":").append(p.getId()).append(",") // o p.getId()
                        .append("\"division\":\"").append(p.getDivision()).append("\",")
                        .append("\"fechaInicio\":\"").append(p.getFechaInicio()).append("\",")
                        .append("\"fechaFin\":\"").append(p.getFechaFin()).append("\",")
                        .append("\"activo\":").append(p.isActivo())
                        .append("}");

                if (i < listaPeriodos.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(json.toString());
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}