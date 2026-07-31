package mx.edu.utez.DesarrolloAcademico.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarDesarrollador_Dao;


    @WebServlet(name = "AgregarDesarrolladorServlet", value = "/AgregarDesarrolladorServlet")

    @MultipartConfig
    public class AgregarDesarrolladorServlet extends HttpServlet {

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();

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
            if (nombre == null || nombre.trim().isEmpty()
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

            if (!contrasena.equals(confirmarContrasena)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Las contraseñas no coinciden.\"}");
                out.flush();
                return;
            }

            int idDivision;
            try {
                idDivision = Integer.parseInt(divisionStr.trim());
            } catch (NumberFormatException ex) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"División inválida.\"}");
                out.flush();
                return;
            }

            AgregarDesarrollador_Dao dao = new AgregarDesarrollador_Dao();

            if (dao.existeCorreoOEmpleado(correo.trim(), numeroEmpleado.trim())) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.write("{\"success\": false, \"message\": \"Ya existe un usuario con ese correo o número de empleado.\"}");
                out.flush();
                return;
            }

            HttpSession session = request.getSession(false);
            Usuario usuarioSesion = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
            Integer creadoPor = (usuarioSesion != null) ? usuarioSesion.getIdUsuario() : null;

            Usuario nuevoDesarrollador = new Usuario();
            nuevoDesarrollador.setNombre(nombre.trim());
            nuevoDesarrollador.setApellidoPaterno(apellidoPaterno.trim());
            nuevoDesarrollador.setApellidoMaterno(apellidoMaterno.trim());
            nuevoDesarrollador.setRol("desarrollo"); // Debe coincidir con lo que espera LoginServlet
            nuevoDesarrollador.setIdDivision(idDivision);
            nuevoDesarrollador.setNumeroEmpleado(numeroEmpleado.trim());
            nuevoDesarrollador.setTelefono(telefono.trim());
            nuevoDesarrollador.setCorreoInstitucional(correo.trim());
            nuevoDesarrollador.setContrasena(contrasena); // TODO: hashear con BCrypt antes de guardar
            nuevoDesarrollador.setCreadoPor(creadoPor);

            boolean exito = dao.registrarDesarrollador(nuevoDesarrollador);

            if (exito) {
                out.write("{\"success\": true, \"message\": \"Desarrollador registrado correctamente.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"No se pudo guardar el desarrollador en la base de datos.\"}");
            }
            out.flush();
        }
    }
