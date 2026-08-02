package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioDao;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AgregarUsuarioServlet", value = "/AgregarUsuarioServlet")
public class AgregarUsuarioServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                out.write("{\"success\": false, \"message\": \"Sesión inválida o expirada.\"}");
                return;
            }
            Usuario adminUser = (Usuario) session.getAttribute("usuario");
            int idCreador = adminUser.getIdUsuario();

            String nombre = request.getParameter("nombre");
            String apellidoPaterno = request.getParameter("apellido_paterno");
            String apellidoMaterno = request.getParameter("apellido_materno");
            String numeroEmpleado = request.getParameter("numero_empleado");
            String correoInstitucional = request.getParameter("correo");
            String telefono = request.getParameter("telefono");
            String divisionStr = request.getParameter("division");
            String contra = request.getParameter("contrasena");
            String confirmContra = request.getParameter("confirmar_contrasena");
            String rol = request.getParameter("rol");

            if (nombre == null || nombre.trim().isEmpty() ||
                apellidoPaterno == null || apellidoPaterno.trim().isEmpty() ||
                numeroEmpleado == null || numeroEmpleado.trim().isEmpty() ||
                correoInstitucional == null || correoInstitucional.trim().isEmpty() ||
                divisionStr == null || divisionStr.trim().isEmpty() ||
                contra == null || contra.isEmpty() ||
                rol == null || rol.trim().isEmpty()) {
                
                out.write("{\"success\": false, \"message\": \"Faltan campos obligatorios.\"}");
                return;
            }

            nombre = nombre.trim();
            apellidoPaterno = apellidoPaterno.trim();
            apellidoMaterno = apellidoMaterno != null ? apellidoMaterno.trim() : "";
            numeroEmpleado = numeroEmpleado.trim();
            correoInstitucional = correoInstitucional.trim();
            telefono = telefono != null ? telefono.trim() : "";
            rol = rol.trim().toLowerCase();

            if (!"docente".equals(rol) && !"coordinador".equals(rol)) {
                out.write("{\"success\": false, \"message\": \"Rol inválido.\"}");
                return;
            }

            int idDivision = 0;
            switch(divisionStr) {
                case "DACEA": idDivision = 1; break;
                case "DAMI": idDivision = 2; break;
                case "DATID": idDivision = 3; break;
                case "DATEFI": idDivision = 4; break;
                default: 
                    try {
                        idDivision = Integer.parseInt(divisionStr);
                    } catch (NumberFormatException e) {
                        out.write("{\"success\": false, \"message\": \"División inválida.\"}");
                        return;
                    }
            }

            if (!contra.equals(confirmContra)) {
                out.write("{\"success\": false, \"message\": \"Las contraseñas no coinciden.\"}");
                return;
            }

            if (usuarioDao.buscarPorEmailOEmpleado(correoInstitucional) != null) {
                out.write("{\"success\": false, \"message\": \"El correo institucional ya está registrado.\"}");
                return;
            }
            if (usuarioDao.buscarPorEmailOEmpleado(numeroEmpleado) != null) {
                out.write("{\"success\": false, \"message\": \"El número de empleado ya está registrado.\"}");
                return;
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidoPaterno(apellidoPaterno);
            nuevoUsuario.setApellidoMaterno(apellidoMaterno);
            nuevoUsuario.setRol(rol);
            nuevoUsuario.setIdDivision(idDivision);
            nuevoUsuario.setNumeroEmpleado(numeroEmpleado);
            nuevoUsuario.setTelefono(telefono);
            nuevoUsuario.setCorreoInstitucional(correoInstitucional);
            nuevoUsuario.setContrasena(contra);
            nuevoUsuario.setCreadoPor(idCreador);

            boolean registrado = usuarioDao.registrarUsuario(nuevoUsuario);

            if (registrado) {
                out.write("{\"success\": true, \"message\": \"Usuario registrado exitosamente.\"}");
            } else {
                out.write("{\"success\": false, \"message\": \"No se pudo guardar el usuario en la BD.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Error del servidor: " + e.getMessage() + "\"}");
        }
        out.flush();
    }
}
