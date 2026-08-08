package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.ConstanciaDao;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioDao;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet(name = "SubirConstanciaServlet1", value = "/SubirConstanciaServlet1")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 15    // 15MB
)
public class SubirConstanciaServlet1 extends HttpServlet {

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

        try {
            String idEventoStr = request.getParameter("idEvento");
            String vigencia = request.getParameter("vigencia"); // "si" o "no"
            String fechaVencimiento = request.getParameter("fechaVencimiento");

            if (idEventoStr == null || idEventoStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Falta el ID del evento.\"}");
                out.flush();
                return;
            }

            int idEvento = Integer.parseInt(idEventoStr);
            boolean tieneVigencia = "si".equalsIgnoreCase(vigencia);

            ConstanciaDao dao = new ConstanciaDao();
            UsuarioDao usuarioDao = new UsuarioDao();

            // Garantiza crear o reutilizar el participante
            int idParticipante = usuarioDao.obtenerOCrearParticipante(idEvento, usuario.getIdUsuario());

            if (idParticipante == -1) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"Error al vincular el usuario con el evento.\"}");
                out.flush();
                return;
            }

            if (dao.verificarConstanciaExistente(idParticipante)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.write("{\"success\": false, \"message\": \"Ya has subido una constancia para este evento.\"}");
                out.flush();
                return;
            }

            Part filePart = request.getPart("archivo");
            if (filePart == null || filePart.getSize() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Debes seleccionar un archivo PDF, PNG o JPG.\"}");
                out.flush();
                return;
            }

            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String fileNameLower = fileName.toLowerCase();
            if (!fileNameLower.endsWith(".pdf") && !fileNameLower.endsWith(".png")
                    && !fileNameLower.endsWith(".jpg") && !fileNameLower.endsWith(".jpeg")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"El archivo debe ser PDF, PNG o JPG.\"}");
                out.flush();
                return;
            }

            // Guardado físico
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads" + File.separator + "constancias";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            String filePath = uploadPath + File.separator + uniqueFileName;

            filePart.write(filePath);

            String rutaRelativa = "uploads/constancias/" + uniqueFileName;

            boolean exito = dao.guardarConstancia(idParticipante, rutaRelativa, fileName, tieneVigencia, fechaVencimiento, usuario.getIdUsuario());

            if (exito) {
                out.write("{\"success\": true, \"message\": \"Constancia subida correctamente.\"}");
            } else {
                new File(filePath).delete();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"Error al guardar en base de datos.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Error en el servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}