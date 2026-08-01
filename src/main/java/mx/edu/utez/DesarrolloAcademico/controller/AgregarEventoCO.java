package mx.edu.utez.DesarrolloAcademico.controller;

import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.agregarEvento_co;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarEvento_Co;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AgregarEventoCO", value = "/AgregarEventoCO")
@MultipartConfig
public class AgregarEventoCO extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String nombre = request.getParameter("nombre");
        String lugar = request.getParameter("lugar");
        String institucion = request.getParameter("institucion");
        String tipo = request.getParameter("tipo");
        if (tipo != null) {
            tipo = tipo.trim().toLowerCase(java.util.Locale.ROOT);
        }
        String descripcion = request.getParameter("descripcion");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String modalidad = request.getParameter("modalidad");

        String[] docentesIdsStr = request.getParameterValues("docentesSeleccionados");
        List<Integer> docentesAsignados = new ArrayList<>();
        if (docentesIdsStr != null) {
            for (String idStr : docentesIdsStr) {
                try {
                    docentesAsignados.add(Integer.parseInt(idStr));
                } catch (NumberFormatException ignored) {
                }
            }
        }

        PrintWriter out = response.getWriter();


        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        int idDivision = (usuario != null && usuario.getIdDivision() != null) ? usuario.getIdDivision() : 1;
        int creadoPor = (usuario != null) ? usuario.getIdUsuario() : 1;

        // Validación mínima en servidor (nunca confiar solo en el "required" del HTML)
        if (nombre == null || nombre.trim().isEmpty()
                || lugar == null || lugar.trim().isEmpty()
                || fechaInicio == null || fechaInicio.trim().isEmpty()
                || fechaFin == null || fechaFin.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\": false, \"message\": \"Faltan campos obligatorios.\"}");
            out.flush();
            return;
        }

        try {
            java.time.LocalDate inicio = java.time.LocalDate.parse(fechaInicio);
            java.time.LocalDate fin = java.time.LocalDate.parse(fechaFin);
            if (!fin.isAfter(inicio)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"La fecha de fin debe ser posterior a la fecha de inicio.\"}");
                out.flush();
                return;
            }
        } catch (java.time.format.DateTimeParseException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\": false, \"message\": \"Formato de fecha inválido.\"}");
            out.flush();
            return;
        }

        agregarEvento_co nuevoEvento = new agregarEvento_co();
        nuevoEvento.setNombre(nombre);
        nuevoEvento.setLugar(lugar);
        nuevoEvento.setInstitucion(institucion);
        nuevoEvento.setTipo(tipo);
        nuevoEvento.setDescripcion(descripcion);
        nuevoEvento.setFechaInicio(fechaInicio);
        nuevoEvento.setFechaFin(fechaFin);
        nuevoEvento.setModalidad(modalidad);
        nuevoEvento.setDocentesAsignados(docentesAsignados);
        nuevoEvento.setIdDivision(idDivision);
        nuevoEvento.setCreadoPor(creadoPor);

        AgregarEvento_Co dao = new AgregarEvento_Co();
        boolean exito = dao.registrarEvento(nuevoEvento);

        if (exito) {
            out.write("{\"success\": true, \"message\": \"Evento registrado correctamente.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"No se pudo guardar el evento en la base de datos.\"}");
        }
        out.flush();
    }
}