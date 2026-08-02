package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioListaDao;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RemoverDocenteEventoServlet", value = "/RemoverDocenteEventoServlet")
public class RemoverDocenteEventoServlet extends HttpServlet {

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

        try {
            int idEvento = Integer.parseInt(request.getParameter("idEvento"));
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

            boolean ok = dao.removerParticipante(idEvento, idUsuario);
            if (ok) {
                out.write("{\"success\":true}");
            } else {
                out.write("{\"success\":false,\"message\":\"No se pudo remover.\"}");
            }
        } catch (Exception e) {
            out.write("{\"success\":false,\"message\":\"Datos inválidos.\"}");
        }
        out.flush();
    }
}
