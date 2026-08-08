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

@WebServlet(name = "EditarDocente", value = "/EditarDocente")
@MultipartConfig
public class EditarDocente extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/vista_general_desarrollador_de.jsp");
            return;
        }

        try {
            int id = Integer.parseInt(idParam.trim());
            AgregarDesarrollador_Dao dao = new AgregarDesarrollador_Dao();
            Usuario dev = dao.obtenerPorId(id);

            if (dev == null) {
                response.sendRedirect(request.getContextPath() + "/vista_general_desarrollador_de.jsp");
                return;
            }

            request.setAttribute("dev", dev);
            request.setAttribute("docente", dev);

            request.getRequestDispatcher("editar_docente_de.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/vista_general_desarrollador_de.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Soporta 'id' o 'idUsuario' / 'id_usuario'
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) idStr = request.getParameter("idUsuario");
            if (idStr == null || idStr.trim().isEmpty()) idStr = request.getParameter("id_usuario");

            String nombre = request.getParameter("nombre");
            String apellidoPaterno = request.getParameter("apellidoPaterno");
            if (apellidoPaterno == null) apellidoPaterno = request.getParameter("apellido_paterno");

            String apellidoMaterno = request.getParameter("apellidoMaterno");
            if (apellidoMaterno == null) apellidoMaterno = request.getParameter("apellido_materno");

            String divisionStr = request.getParameter("idDivision");
            if (divisionStr == null) divisionStr = request.getParameter("division");

            String numeroEmpleado = request.getParameter("numeroEmpleado");
            if (numeroEmpleado == null) numeroEmpleado = request.getParameter("numero_empleado");

            String telefono = request.getParameter("telefono");

            String correo = request.getParameter("correoInstitucional");
            if (correo == null) correo = request.getParameter("correo");

            String contrasena = request.getParameter("contrasena");

            // Soporta variantes de nombre para confirmación de contraseña
            String confirmarContrasena = request.getParameter("confirmarContrasena");
            if (confirmarContrasena == null) {
                confirmarContrasena = request.getParameter("confirmar_contrasena");
            }

            // Validar campos requeridos básicos
            if (idStr == null || nombre == null || apellidoPaterno == null ||
                    divisionStr == null || numeroEmpleado == null || telefono == null || correo == null) {
                out.write("{\"success\": false, \"message\": \"Campos requeridos faltantes.\"}");
                return;
            }

            // Validar cambio opcional de contraseña
            if (contrasena != null && !contrasena.trim().isEmpty()) {
                if (contrasena.trim().length() < 8) {
                    out.write("{\"success\": false, \"message\": \"La contraseña debe tener al menos 8 caracteres.\"}");
                    return;
                }
                if (!contrasena.trim().equals(confirmarContrasena != null ? confirmarContrasena.trim() : "")) {
                    out.write("{\"success\": false, \"message\": \"Las contraseñas no coinciden.\"}");
                    return;
                }
                contrasena = contrasena.trim();
            } else {
                contrasena = null;
            }

            int id = Integer.parseInt(idStr.trim());
            int idDivision = Integer.parseInt(divisionStr.trim());

            Usuario dev = new Usuario();
            dev.setIdUsuario(id);
            dev.setNombre(nombre.trim());
            dev.setApellidoPaterno(apellidoPaterno.trim());
            dev.setApellidoMaterno(apellidoMaterno != null ? apellidoMaterno.trim() : "");
            dev.setIdDivision(idDivision);
            dev.setNumeroEmpleado(numeroEmpleado.trim());
            dev.setTelefono(telefono.trim());
            dev.setCorreoInstitucional(correo.trim());

            AgregarDesarrollador_Dao dao = new AgregarDesarrollador_Dao();

            // Aquí se ejecuta la verificación del DAO
            boolean actualizado = dao.actualizarDesarrollador(dev, contrasena);

            if (actualizado) {
                out.write("{\"success\": true, \"message\": \"Docente actualizado con éxito.\"}");
            } else {
                out.write("{\"success\": false, \"message\": \"No se encontró el registro para actualizar.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Limpia el mensaje para no romper el formato JSON
            String msgError = e.getMessage() != null ? e.getMessage().replace("\"", "'").replace("\n", " ") : "Error interno del servidor.";
            out.write("{\"success\": false, \"message\": \"" + msgError + "\"}");
        }
        out.flush();
    }
}