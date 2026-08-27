package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.agregarEvento_co;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarEvento_Co;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * ARCHIVO COMPLETO. Reemplaza el tuyo entero, no lo parchees.
 *
 * GET  /EditarEventoServlet?id=123  -> JSON con los datos del evento (incluye idDivision)
 * POST /EditarEventoServlet         -> guarda los cambios (incluye division y docentes)
 */
@WebServlet(name = "EditarEventoServlet", value = "/EditarEventoServlet")
public class EditarEventoServlet extends HttpServlet {

    private String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // ------------------------------------------------------------------
    // GET: regresa los datos de UN evento para llenar el formulario
    // ------------------------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
                        + "\"modalidad\":\"" + escapar(evento.getModalidad()) + "\","
                        + "\"idDivision\":" + evento.getIdDivision()
                        + "}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Error inesperado en el servidor: " + e.getMessage() + "\"}");
        }
        out.flush();
    }

    // ------------------------------------------------------------------
    // POST: guarda los cambios del formulario de edición
    // ------------------------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String idStr        = request.getParameter("id");
            String nombre       = request.getParameter("nombre");
            String lugar        = request.getParameter("lugar");
            String institucion  = request.getParameter("institucion");
            String tipo         = request.getParameter("tipo");
            String descripcion  = request.getParameter("descripcion");
            String fechaInicio  = request.getParameter("fechaInicio");
            String fechaFin     = request.getParameter("fechaFin");
            String modalidad    = request.getParameter("modalidad");
            String divisionStr  = request.getParameter("division");

            if (tipo != null) {
                tipo = tipo.trim().toLowerCase(java.util.Locale.ROOT);
            }

            // --- Validación de campos obligatorios ---
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

            // --- Validación de fechas ---
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

            // --- Validación de división (declarada UNA sola vez) ---
            if (divisionStr == null || divisionStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Debes seleccionar una división.\"}");
                out.flush();
                return;
            }

            int idDivision;
            try {
                idDivision = Integer.parseInt(divisionStr.trim());
            } catch (NumberFormatException nfe) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"División inválida.\"}");
                out.flush();
                return;
            }

            int idEvento = Integer.parseInt(idStr.trim());

            // --- Docentes asignados (opcional: solo si el form los manda) ---
            String[] docentesParam = request.getParameterValues("docentes");
            List<Integer> idsDocentes = null;
            if (docentesParam != null) {
                idsDocentes = new ArrayList<>();
                for (String d : docentesParam) {
                    if (d == null || d.trim().isEmpty()) continue;
                    try {
                        idsDocentes.add(Integer.parseInt(d.trim()));
                    } catch (NumberFormatException ignore) {
                        // id inválido: lo saltamos
                    }
                }
            }

            // --- Armar el objeto y guardar ---
            agregarEvento_co evento = new agregarEvento_co();
            evento.setId(idEvento);
            evento.setNombre(nombre);
            evento.setLugar(lugar);
            evento.setInstitucion(institucion);
            evento.setTipo(tipo);
            evento.setDescripcion(descripcion);
            evento.setFechaInicio(fechaInicio);
            evento.setFechaFin(fechaFin);
            evento.setModalidad(modalidad);
            evento.setIdDivision(idDivision);

            AgregarEvento_Co dao = new AgregarEvento_Co();
            boolean exito = dao.actualizarEvento(evento);

            // Solo sincronizamos docentes si el formulario los envió
            if (exito && idsDocentes != null) {
                dao.actualizarDocentesEvento(idEvento, idsDocentes);
            }

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