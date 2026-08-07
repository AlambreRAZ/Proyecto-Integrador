package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

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
            String idEvento = request.getParameter("idEvento");
            boolean tieneVigencia = Boolean.parseBoolean(request.getParameter("tieneVigencia"));
            String fechaVigencia = request.getParameter("fechaVigencia");

            Part filePart = request.getPart("archivo");
            String fileName = filePart.getSubmittedFileName();

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
            if (!uploadDir.exists()) uploadDir.mkdir();

            // Guardar archivo en disco
            String filePath = uploadPath + File.separator + System.currentTimeMillis() + "_" + fileName;
            filePart.write(filePath);


            out.print("{\"success\": true, \"message\": \"Archivo subido con exito.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Error interno en el servidor: " + e.getMessage() + "\"}");
        }
    }
}
