package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import mx.edu.utez.DesarrolloAcademico.model.dao.PeriodoCargaDao;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/CargarArchivo")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB máximo por archivo
        maxRequestSize = 1024 * 1024 * 50    // 50MB máximo petición
)
public class CargarArchivo extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String idEventoStr = request.getParameter("idEvento");
            boolean tieneVigencia = Boolean.parseBoolean(request.getParameter("tieneVigencia"));
            String fechaVigencia = request.getParameter("fechaVigencia");

            if (idEventoStr == null || idEventoStr.trim().isEmpty()) {
                out.print("{\"success\": false, \"message\": \"No se recibió el ID del evento.\"}");
                return;
            }
            int idEvento = Integer.parseInt(idEventoStr.trim());

            // ===========================================================
            // VALIDACIÓN NUEVA: periodo de carga de la división del evento
            // Si el periodo no está habilitado -> NO se permite adjuntar.
            // ===========================================================
            PeriodoCargaDao periodoDao = new PeriodoCargaDao();
            Integer idDivision = periodoDao.obtenerDivisionEvento(idEvento);

            if (idDivision == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print("{\"success\": false, \"message\": \"El evento no tiene división asignada.\"}");
                return;
            }
            if (!periodoDao.hayPeriodoActivo(idDivision)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                String nombreDiv = periodoDao.obtenerNombreDivision(idDivision);
                out.print("{\"success\": false, \"message\": \"El periodo de carga de " + nombreDiv +
                        " no está habilitado. No es posible adjuntar evidencia.\"}");
                return;
            }
            // ===========================================================

            Part filePart = request.getPart("archivo");
            String fileName = (filePart != null) ? filePart.getSubmittedFileName() : null;

            if (fileName == null || fileName.isEmpty()) {
                out.print("{\"success\": false, \"message\": \"No se adjuntó ningún archivo.\"}");
                return;
            }

            // Validar extensión en Backend
            String lowerName = fileName.toLowerCase();
            if (!lowerName.endsWith(".pdf") && !lowerName.endsWith(".jpg") &&
                    !lowerName.endsWith(".jpeg") && !lowerName.endsWith(".png")) {
                out.print("{\"success\": false, \"message\": \"Formato de archivo invalido. Solo PDF, JPG y PNG.\"}");
                return;
            }

            // Carpeta donde se guardarán los archivos en el servidor
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            // Guardar archivo en disco
            String filePath = uploadPath + File.separator + System.currentTimeMillis() + "_" + fileName;
            filePart.write(filePath);

            out.print("{\"success\": true, \"message\": \"Archivo subido con exito.\"}");

        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"message\": \"ID de evento inválido.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Error interno en el servidor: " + e.getMessage() + "\"}");
        }
    }
}