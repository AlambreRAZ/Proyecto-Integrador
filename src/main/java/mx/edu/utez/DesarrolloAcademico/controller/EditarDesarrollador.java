package mx.edu.utez.DesarrolloAcademico.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarDesarrollador_Dao;

import java.io.IOException;
import java.io.PrintWriter;





@WebServlet(name = "EditarDesarrollador", value = "/EditarDesarrollador")
@MultipartConfig
public class EditarDesarrollador extends HttpServlet {

    private String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // Regresa los datos de UN desarrollador en JSON, para llenar el formulario de edición.
    // La contraseña nunca se incluye en la respuesta.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            AgregarDesarrollador_Dao dao = new AgregarDesarrollador_Dao();
            Usuario desarrollador = dao.obtenerPorId(id);

            if (desarrollador == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"success\": false, \"message\": \"Desarrollador no encontrado.\"}");
            } else {
                out.write("{"
                        + "\"success\": true,"
                        + "\"id\":" + desarrollador.getIdUsuario() + ","
                        + "\"nombre\":\"" + escapar(desarrollador.getNombre()) + "\","
                        + "\"apellidoPaterno\":\"" + escapar(desarrollador.getApellidoPaterno()) + "\","
                        + "\"apellidoMaterno\":\"" + escapar(desarrollador.getApellidoMaterno()) + "\","
                        + "\"idDivision\":" + (desarrollador.getIdDivision() != null ? desarrollador.getIdDivision() : "null") + ","
                        + "\"numeroEmpleado\":\"" + escapar(desarrollador.getNumeroEmpleado()) + "\","
                        + "\"telefono\":\"" + escapar(desarrollador.getTelefono()) + "\","
                        + "\"correo\":\"" + escapar(desarrollador.getCorreoInstitucional()) + "\","
                        + "\"activo\":" + desarrollador.getActivo()
                        + "}");
            }
        } catch (NumberFormatException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\": false, \"message\": \"Id de desarrollador inválido.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Error inesperado en el servidor: " + escapar(e.getMessage()) + "\"}");
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
            String apellidoPaterno = request.getParameter("apellido_paterno");
            String apellidoMaterno = request.getParameter("apellido_materno");
            String divisionStr = request.getParameter("division");
            String numeroEmpleado = request.getParameter("numero_empleado");
            String telefono = request.getParameter("telefono");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");
            String confirmarContrasena = request.getParameter("confirmar_contrasena");

            // Validación mínima en servidor (nunca confiar solo en el "required" del HTML)
            if (idStr == null || idStr.trim().isEmpty()
                    || nombre == null || nombre.trim().isEmpty()
                    || apellidoPaterno == null || apellidoPaterno.trim().isEmpty()
                    || apellidoMaterno == null || apellidoMaterno.trim().isEmpty()
                    || divisionStr == null || divisionStr.trim().isEmpty()
                    || numeroEmpleado == null || numeroEmpleado.trim().isEmpty()
                    || telefono == null || telefono.trim().isEmpty()
                    || correo == null || correo.trim().isEmpty()
                    || contrasena == null || contrasena.isEmpty()
                    || confirmarContrasena == null || confirmarContrasena.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Faltan campos obligatorios.\"}");
                out.flush();
                return;
            }

            if (contrasena.length() < 8) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"La contraseña debe tener al menos 8 caracteres.\"}");
                out.flush();
                return;
            }

            if (!contrasena.equals(confirmarContrasena)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Las contraseñas no coinciden.\"}");
                out.flush();
                return;
            }

            int id;
            int idDivision;
            try {
                id = Integer.parseInt(idStr.trim());
                idDivision = Integer.parseInt(divisionStr.trim());
            } catch (NumberFormatException ex) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Id o división inválidos.\"}");
                out.flush();
                return;
            }

            AgregarDesarrollador_Dao dao = new AgregarDesarrollador_Dao();

            if (dao.existeCorreoOEmpleadoExcluyendo(correo.trim(), numeroEmpleado.trim(), id)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.write("{\"success\": false, \"message\": \"Ya existe otro usuario con ese correo o número de empleado.\"}");
                out.flush();
                return;
            }

            Usuario desarrollador = new Usuario();
            desarrollador.setIdUsuario(id);
            desarrollador.setNombre(nombre.trim());
            desarrollador.setApellidoPaterno(apellidoPaterno.trim());
            desarrollador.setApellidoMaterno(apellidoMaterno.trim());
            desarrollador.setIdDivision(idDivision);
            desarrollador.setNumeroEmpleado(numeroEmpleado.trim());
            desarrollador.setTelefono(telefono.trim());
            desarrollador.setCorreoInstitucional(correo.trim());

            boolean exito = dao.actualizarDesarrollador(desarrollador, contrasena);

            if (exito) {
                out.write("{\"success\": true, \"message\": \"Desarrollador actualizado correctamente.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"No se pudo actualizar el desarrollador en la base de datos.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Error inesperado en el servidor: " + escapar(e.getMessage()) + "\"}");
        }
        out.flush();
    }
}