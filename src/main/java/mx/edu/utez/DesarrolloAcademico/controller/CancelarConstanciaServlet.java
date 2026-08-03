package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.ConstanciaDao;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CancelarConstanciaServlet", value = "/CancelarConstanciaServlet")
public class CancelarConstanciaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        String idConstanciaStr = request.getParameter("idConstancia");
        String idEventoStr = request.getParameter("idEvento");

        if (idConstanciaStr == null || idEventoStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\": false, \"message\": \"Faltan parámetros.\"}");
            out.flush();
            return;
        }

        try {
            int idConstancia = Integer.parseInt(idConstanciaStr);
            int idEvento = Integer.parseInt(idEventoStr);

            ConstanciaDao dao = new ConstanciaDao();
            int idParticipante = dao.obtenerIdParticipante(idEvento, usuario.getIdUsuario());

            if (idParticipante == -1) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.write("{\"success\": false, \"message\": \"No estás asignado a este evento.\"}");
                out.flush();
                return;
            }

            // Eliminar de BD y obtener ruta del archivo para borrarlo del disco
            String rutaRelativa = dao.eliminarConstancia(idConstancia, idParticipante);

            if (rutaRelativa == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"success\": false, \"message\": \"No se encontró la constancia.\"}");
                out.flush();
                return;
            }

            // Borrar el archivo físico del servidor
            String rutaAbsoluta = getServletContext().getRealPath("") + File.separator + rutaRelativa.replace("/", File.separator);
            File archivo = new File(rutaAbsoluta);
            if (archivo.exists()) {
                archivo.delete();
            }

            out.write("{\"success\": true, \"message\": \"Entrega cancelada correctamente.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}
