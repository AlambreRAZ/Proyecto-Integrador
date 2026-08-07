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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                AgregarDesarrollador_Dao dao = new AgregarDesarrollador_Dao();
                Usuario desarrollador = dao.obtenerPorId(id);

                if (desarrollador != null) {
                    request.setAttribute("dev", desarrollador);
                    request.getRequestDispatcher("/editar_desarrollador_de.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                System.err.println("ID inválido: " + idStr);
            }
        }

        response.sendRedirect(request.getContextPath() + "/gestion_desarrolladores_de.jsp");
    }

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

            // Validaciones en servidor
            if (idStr == null || idStr.trim().isEmpty()
                    || nombre == null || nombre.trim().isEmpty()
                    || apellidoPaterno == null || apellidoPaterno.trim().isEmpty()
                    || apellidoMaterno == null || apellidoMaterno.trim().isEmpty()
                    || divisionStr == null || divisionStr.trim().isEmpty()
                    || numeroEmpleado == null || numeroEmpleado.trim().isEmpty()
                    || telefono == null || telefono.trim().isEmpty()
                    || correo == null || correo.trim().isEmpty()
                    || contrasena == null || contrasena.trim().isEmpty()
                    || confirmarContrasena == null || confirmarContrasena.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Faltan campos obligatorios.\"}");
                out.flush();
                return;
            }

            // Correo institucional
            if (!correo.trim().toLowerCase().endsWith("@utez.edu.mx")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"El correo debe terminar en @utez.edu.mx\"}");
                out.flush();
                return;
            }

            // Longitud de 12 a 15 caracteres
            if (contrasena.trim().length() < 12 || contrasena.trim().length() > 15) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"La contraseña debe tener entre 12 y 15 caracteres.\"}");
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

            boolean exito = dao.actualizarDesarrollador(desarrollador, contrasena.trim());

            if (exito) {
                out.write("{\"success\": true, \"message\": \"Desarrollador actualizado correctamente.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"No se pudo actualizar en la base de datos.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Error inesperado: " + escapar(e.getMessage()) + "\"}");
        }
        out.flush();
    }
}