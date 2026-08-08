package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mx.edu.utez.DesarrolloAcademico.model.Periodo;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioListaDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/AgregarPeriodoServlet")
public class AgregarPeriodoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            HttpSession session = request.getSession(false);
            Usuario usuarioActual = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

            if (usuarioActual == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Sesión no válida o expirada.\"}");
                return;
            }

            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String body = buffer.toString();

            String division = extraerValor(body, "division");
            String fechaInicioStr = extraerValor(body, "fechaInicio");
            String fechaFinStr = extraerValor(body, "fechaFin");

            if (division == null || fechaInicioStr == null || fechaFinStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Datos incompletos en el formulario.\"}");
                return;
            }

            UsuarioListaDao dao = new UsuarioListaDao();

            // Validar Duplicado en Agregar (ID = 0)
            if (dao.existeDivision(division, 0)) {
                String nombreDivision = dao.obtenerNombreDivision(division);
                response.setStatus(HttpServletResponse.SC_CONFLICT); // Código HTTP 409
                response.getWriter().write("{\"status\":\"duplicate\", \"message\":\"La división " + nombreDivision + " ya tiene un periodo de carga registrado.\"}");
                return;
            }

            Periodo periodo = new Periodo();
            periodo.setDivision(division);
            periodo.setFechaInicio(Date.valueOf(fechaInicioStr));
            periodo.setFechaFin(Date.valueOf(fechaFinStr));
            periodo.setActivo(true);

            int idUsuario = usuarioActual.getIdUsuario();
            boolean exito = dao.registrarPeriodo(periodo, idUsuario);

            if (exito) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"status\":\"success\", \"message\":\"Periodo registrado correctamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"No se pudo registrar el periodo en la base de datos\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errorClean = e.getMessage() != null ? e.getMessage().replace("\"", "'").replace("\r", "").replace("\n", " ") : "Error interno del servidor";
            response.getWriter().write("{\"status\":\"error\", \"message\":\"" + errorClean + "\"}");
        }
    }

    private String extraerValor(String json, String clave) {
        String patron = "\"" + clave + "\":\"";
        int inicio = json.indexOf(patron);
        if (inicio == -1) return null;
        inicio += patron.length();
        int fin = json.indexOf("\"", inicio);
        if (fin == -1) return null;
        return json.substring(inicio, fin);
    }
}