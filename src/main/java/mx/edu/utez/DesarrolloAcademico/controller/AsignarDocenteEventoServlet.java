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
            out.write("{\"success\":false,\"message\":\"Sesión expirada. Por favor vuelve a iniciar sesión.\"}");
            return;
        }

        Usuario usuarioActivo = (Usuario) session.getAttribute("usuario");

        try {
            String idEventoParam = request.getParameter("idEvento");
            String correo = request.getParameter("correo");

            if (idEventoParam == null || idEventoParam.trim().isEmpty() || idEventoParam.equals("0")) {
                out.write("{\"success\":false,\"message\":\"No se detectó el ID del evento actual.\"}");
                return;
            }

            int idEvento = Integer.parseInt(idEventoParam);

            // 1. Buscamos si el correo existe en el sistema
            int idUsuario = dao.obtenerIdPorCorreo(correo);

            // 2. Si NO existe, mandamos la alerta personalizada
            if (idUsuario <= 0) {
                out.write("{\"success\":false,\"message\":\"El docente no se encuentra registrado en el sistema. Verifica el correo institucional.\"}");
                return;
            }

            // 3. Si SÍ existe, intentamos asignarlo al evento
            boolean ok = dao.asignarParticipante(idEvento, idUsuario, usuarioActivo.getIdUsuario());

            if (ok) {
                out.write("{\"success\":true}");
            } else {
                out.write("{\"success\":false,\"message\":\"El docente ya se encuentra asignado a este evento.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\":false,\"message\":\"Error en el servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}