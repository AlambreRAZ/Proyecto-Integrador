package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioDao;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ObtenerDocente", value = "/ObtenerDocente")
public class ObtenerDocente extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            out.write("{\"success\": false, \"message\": \"ID no proporcionado\"}");
            return;
        }

        try {
            int idUsuario = Integer.parseInt(idStr);
            UsuarioDao dao = new UsuarioDao();
            Usuario d = dao.obtenerDocentePorId(idUsuario);

            if (d != null) {
                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"success\": true,");
                json.append("\"idUsuario\":").append(d.getIdUsuario()).append(",");
                json.append("\"nombre\":\"").append(escape(d.getNombre())).append("\",");
                json.append("\"apellidoPaterno\":\"").append(escape(d.getApellidoPaterno())).append("\",");
                json.append("\"apellidoMaterno\":\"").append(escape(d.getApellidoMaterno())).append("\",");
                json.append("\"correoInstitucional\":\"").append(escape(d.getCorreoInstitucional())).append("\",");
                json.append("\"idDivision\":").append(d.getIdDivision()).append(",");
                json.append("\"numeroEmpleado\":\"").append(escape(d.getNumeroEmpleado())).append("\",");
                json.append("\"telefono\":\"").append(escape(d.getTelefono())).append("\",");
                json.append("\"activo\":").append(d.getActivo()).append(",");
                json.append("\"contrasena\":\"").append(escape(d.getContrasena())).append("\"");
                json.append("}");

                out.write(json.toString());
            } else {
                out.write("{\"success\": false, \"message\": \"Docente no encontrado en BD\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Error interno: " + escape(e.getMessage()) + "\"}");
        }
    }

    private String escape(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}