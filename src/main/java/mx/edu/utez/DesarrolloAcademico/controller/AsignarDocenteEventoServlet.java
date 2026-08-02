package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioListaDao;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AsignarDocenteEventoServlet", value = "/AsignarDocenteEventoServlet")
public class AsignarDocenteEventoServlet extends HttpServlet {

    private final UsuarioListaDao dao = new UsuarioListaDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            out.write("{\"success\":false,\"message\":\"No autenticado.\"}");
            return;
        }

        Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");

        try {
            int idEvento = Integer.parseInt(request.getParameter("idEvento"));
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

            boolean ok = dao.asignarParticipante(idEvento, idUsuario, usuarioActivo.getIdUsuario());
            if (ok) {
                out.write("{\"success\":true}");
            } else {
                out.write("{\"success\":false,\"message\":\"No se pudo asignar (quizás ya está asignado).\"}");
            }
        } catch (Exception e) {
            out.write("{\"success\":false,\"message\":\"Datos inválidos.\"}");
        }
        out.flush();
    }
}
