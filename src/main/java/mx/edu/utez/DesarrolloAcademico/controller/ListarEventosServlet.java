package mx.edu.utez.DesarrolloAcademico.controller;

import mx.edu.utez.DesarrolloAcademico.model.agregarEvento_co;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarEvento_Co;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ListarEventosServlet", value = "/ListarEventosServlet")
public class ListarEventosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        AgregarEvento_Co dao = new AgregarEvento_Co();
        List<agregarEvento_co> eventos = dao.listarEventos();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < eventos.size(); i++) {
            agregarEvento_co ev = eventos.get(i);
            if (i > 0) json.append(",");
            json.append("{")
                    .append("\"id\":").append(ev.getId()).append(",")
                    .append("\"nombre\":\"").append(escapar(ev.getNombre())).append("\",")
                    .append("\"lugar\":\"").append(escapar(ev.getLugar())).append("\",")
                    .append("\"institucion\":\"").append(escapar(ev.getInstitucion())).append("\",")
                    .append("\"tipo\":\"").append(escapar(ev.getTipo())).append("\",")
                    .append("\"descripcion\":\"").append(escapar(ev.getDescripcion())).append("\",")
                    .append("\"fechaInicio\":\"").append(escapar(ev.getFechaInicio())).append("\",")
                    .append("\"fechaFin\":\"").append(escapar(ev.getFechaFin())).append("\",")
                    .append("\"modalidad\":\"").append(escapar(ev.getModalidad())).append("\"")
                    .append("}");
        }
        json.append("]");

        out.write(json.toString());
        out.flush();
    }

    private String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
