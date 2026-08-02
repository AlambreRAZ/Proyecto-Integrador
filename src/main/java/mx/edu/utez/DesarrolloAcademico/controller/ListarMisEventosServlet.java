package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.agregarEvento_co;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioListaDao;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ListarMisEventosServlet", value = "/ListarMisEventosServlet")
public class ListarMisEventosServlet extends HttpServlet {

    private final UsuarioListaDao dao = new UsuarioListaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"error\":\"No autenticado\"}");
            out.flush();
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<agregarEvento_co> eventos = dao.listarEventosPorUsuario(usuario.getIdUsuario());

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < eventos.size(); i++) {
            agregarEvento_co ev = eventos.get(i);
            if (i > 0) json.append(",");
            json.append("{")
                .append("\"id\":").append(ev.getId()).append(",")
                .append("\"nombre\":\"").append(esc(ev.getNombre())).append("\",")
                .append("\"tipo\":\"").append(esc(ev.getTipo())).append("\",")
                .append("\"institucion\":\"").append(esc(ev.getInstitucion())).append("\",")
                .append("\"lugar\":\"").append(esc(ev.getLugar())).append("\",")
                .append("\"descripcion\":\"").append(esc(ev.getDescripcion())).append("\",")
                .append("\"fechaInicio\":\"").append(esc(ev.getFechaInicio())).append("\",")
                .append("\"fechaFin\":\"").append(esc(ev.getFechaFin())).append("\",")
                .append("\"modalidad\":\"").append(esc(ev.getModalidad())).append("\"")
                .append("}");
        }
        json.append("]");

        out.write(json.toString());
        out.flush();
    }

    private String esc(String v) {
        if (v == null) return "";
        return v.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
