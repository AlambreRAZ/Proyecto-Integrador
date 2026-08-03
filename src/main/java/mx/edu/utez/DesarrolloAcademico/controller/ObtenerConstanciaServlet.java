package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.ConstanciaDao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

@WebServlet(name = "ObtenerConstanciaServlet", value = "/ObtenerConstanciaServlet")
public class ObtenerConstanciaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"success\": false, \"message\": \"Sesión no válida.\"}");
            out.flush();
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        String idEventoStr = request.getParameter("idEvento");
        if (idEventoStr == null || idEventoStr.isEmpty()) {
            out.write("{\"success\": false, \"message\": \"Falta idEvento.\"}");
            out.flush();
            return;
        }

        try {
            int idEvento = Integer.parseInt(idEventoStr);
            ConstanciaDao dao = new ConstanciaDao();

            int idParticipante = dao.obtenerIdParticipante(idEvento, usuario.getIdUsuario());
            if (idParticipante == -1) {
                out.write("{\"success\": true, \"constancia\": null}");
                out.flush();
                return;
            }

            Map<String, Object> constancia = dao.obtenerConstancia(idParticipante);

            if (constancia == null) {
                out.write("{\"success\": true, \"constancia\": null, \"idParticipante\": " + idParticipante + "}");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Timestamp fechaSubida = (Timestamp) constancia.get("fechaSubida");
                Timestamp fechaVencimiento = (Timestamp) constancia.get("fechaVencimiento");

                String fechaSubidaStr = fechaSubida != null ? sdf.format(fechaSubida) : "";
                String fechaVencimientoStr = fechaVencimiento != null ? sdf.format(fechaVencimiento) : "";
                long fechaVencimientoMs = fechaVencimiento != null ? fechaVencimiento.getTime() : -1;

                out.write("{" +
                    "\"success\": true," +
                    "\"idParticipante\": " + idParticipante + "," +
                    "\"constancia\": {" +
                        "\"idConstancia\": " + constancia.get("idConstancia") + "," +
                        "\"nombreArchivo\": \"" + esc(constancia.get("nombreArchivo").toString()) + "\"," +
                        "\"rutaArchivo\": \"" + esc(constancia.get("rutaArchivo").toString()) + "\"," +
                        "\"tieneVigencia\": " + constancia.get("tieneVigencia") + "," +
                        "\"fechaSubida\": \"" + fechaSubidaStr + "\"," +
                        "\"fechaVencimiento\": \"" + fechaVencimientoStr + "\"," +
                        "\"fechaVencimientoMs\": " + fechaVencimientoMs +
                    "}" +
                "}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
