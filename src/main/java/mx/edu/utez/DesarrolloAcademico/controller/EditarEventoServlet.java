package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.agregarEvento_co;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarEvento_Co;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "EditarEventoServlet", value = "/EditarEventoServlet")
@MultipartConfig
public class EditarEventoServlet extends HttpServlet {

    private String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // Regresa los datos de UN evento en JSON, para llenar el formulario de edición.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            AgregarEvento_Co dao = new AgregarEvento_Co();
            agregarEvento_co evento = dao.obtenerPorId(id);

            if (evento == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"success\": false, \"message\": \"Evento no encontrado.\"}");
            } else {
                out.write("{"
                        + "\"success\": true,"
                        + "\"id\":" + evento.getId() + ","
                        + "\"nombre\":\"" + escapar(evento.getNombre()) + "\","
                        + "\"lugar\":\"" + escapar(evento.getLugar()) + "\","
                        + "\"institucion\":\"" + escapar(evento.getInstitucion()) + "\","
                        + "\"tipo\":\"" + escapar(evento.getTipo()) + "\","
                        + "\"descripcion\":\"" + escapar(evento.getDescripcion()) + "\","
                        + "\"fechaInicio\":\"" + escapar(evento.getFechaInicio()) + "\","
                        + "\"fechaFin\":\"" + escapar(evento.getFechaFin()) + "\","
                        + "\"modalidad\":\"" + escapar(evento.getModalidad()) + "\""
                        + "}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Error inesperado en el servidor: " + e.getMessage() + "\"}");
        }
        out.flush();
    }

    // Guarda los cambios del formulario de edición.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String idStr = request.getParameter("id");
            String nombre = request.getParameter("nombre");
            String lugar = request.getParameter("lugar");
            String institucion = request.getParameter("institucion");
            String tipo = request.getParameter("tipo");
            String descripcion = request.getParameter("descripcion");
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");
            String modalidad = request.getParameter("modalidad");

            if (tipo != null) {
                tipo = tipo.trim().toLowerCase(java.util.Locale.ROOT);
            }

            if (idStr == null || idStr.trim().isEmpty()
                    || nombre == null || nombre.trim().isEmpty()
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

            agregarEvento_co evento = new agregarEvento_co();
            evento.setId(Integer.parseInt(idStr));
            evento.setNombre(nombre);
            evento.setLugar(lugar);
            evento.setInstitucion(institucion);
            evento.setTipo(tipo);
            evento.setDescripcion(descripcion);
            evento.setFechaInicio(fechaInicio);
            evento.setFechaFin(fechaFin);
            evento.setModalidad(modalidad);

            AgregarEvento_Co dao = new AgregarEvento_Co();
            boolean exito = dao.actualizarEvento(evento);

            if (exito) {
                out.write("{\"success\": true, \"message\": \"Evento actualizado correctamente.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"No se pudo actualizar el evento.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Error inesperado en el servidor: " + e.getMessage() + "\"}");
        }
        out.flush();
    }
}