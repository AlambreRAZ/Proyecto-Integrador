package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioDao;

import java.io.IOException;

@WebServlet(name = "RegistroServlet", urlPatterns = {"/registro"})
public class RegistroServlet extends HttpServlet {

    private UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir al JSP de registro por GET
        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String apellidoPaterno = request.getParameter("apellidoPaterno");
        String apellidoMaterno = request.getParameter("apellidoMaterno");
        String numeroEmpleado = request.getParameter("numeroEmpleado");
        String correoInstitucional = request.getParameter("correoInstitucional");
        String telefono = request.getParameter("telefono");
        String divisionStr = request.getParameter("division");
        String contra = request.getParameter("contra");
        String confirmContra = request.getParameter("confirmContra");
        String rol = request.getParameter("rol");

        // 1. Validar que los campos no estén vacíos
        if (nombre == null || nombre.trim().isEmpty() ||
            apellidoPaterno == null || apellidoPaterno.trim().isEmpty() ||
            apellidoMaterno == null || apellidoMaterno.trim().isEmpty() ||
            numeroEmpleado == null || numeroEmpleado.trim().isEmpty() ||
            correoInstitucional == null || correoInstitucional.trim().isEmpty() ||
            telefono == null || telefono.trim().isEmpty() ||
            divisionStr == null || divisionStr.trim().isEmpty() ||
            contra == null || contra.isEmpty() ||
            confirmContra == null || confirmContra.isEmpty() ||
            rol == null || rol.trim().isEmpty()) {
            
            errorResponse(request, response, "Todos los campos son obligatorios.");
            return;
        }

        nombre = nombre.trim();
        apellidoPaterno = apellidoPaterno.trim();
        apellidoMaterno = apellidoMaterno.trim();
        numeroEmpleado = numeroEmpleado.trim();
        correoInstitucional = correoInstitucional.trim();
        telefono = telefono.trim();
        rol = rol.trim().toLowerCase();

        // 2. Validaciones de formato (Regex)
        // Nombres y apellidos: solo letras, espacios y acentos
        String regexNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$";
        if (!nombre.matches(regexNombre) || !apellidoPaterno.matches(regexNombre) || !apellidoMaterno.matches(regexNombre)) {
            errorResponse(request, response, "El nombre y apellidos solo deben contener letras.");
            return;
        }

        // Número de empleado: alfanumérico
        if (!numeroEmpleado.matches("^[a-zA-Z0-9_-]{1,20}$")) {
            errorResponse(request, response, "Número de empleado inválido (máx. 20 caracteres alfanuméricos).");
            return;
        }

        // Correo Institucional: debe ser un correo válido y terminar en utez.edu.mx
        if (!correoInstitucional.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") || 
            (!correoInstitucional.endsWith("@utez.edu.mx") && !correoInstitucional.endsWith("@alumnos.utez.edu.mx"))) {
            errorResponse(request, response, "El correo electrónico debe ser institucional (@utez.edu.mx o @alumnos.utez.edu.mx).");
            return;
        }

        // Teléfono: exactamente 10 dígitos numéricos
        if (!telefono.matches("^[0-9]{10}$")) {
            errorResponse(request, response, "El teléfono debe contener exactamente 10 dígitos numéricos.");
            return;
        }

        // Rol: sólo se permite 'docente' o 'coordinador' en registro público
        if (!"docente".equals(rol) && !"coordinador".equals(rol)) {
            errorResponse(request, response, "Rol de usuario inválido.");
            return;
        }

        // División
        int idDivision;
        try {
            idDivision = Integer.parseInt(divisionStr);
        } catch (NumberFormatException e) {
            errorResponse(request, response, "División seleccionada inválida.");
            return;
        }

        // 3. Confirmar que contraseñas coinciden y cumplen longitud mínima
        if (contra.length() < 4) {
            errorResponse(request, response, "La contraseña debe tener al menos 4 caracteres.");
            return;
        }
        if (!contra.equals(confirmContra)) {
            errorResponse(request, response, "Las contraseñas no coinciden.");
            return;
        }

        // 4. Validar unicidad de Correo y Número de Empleado en la BD
        if (usuarioDao.buscarPorEmailOEmpleado(correoInstitucional) != null) {
            errorResponse(request, response, "El correo institucional ya se encuentra registrado.");
            return;
        }
        if (usuarioDao.buscarPorEmailOEmpleado(numeroEmpleado) != null) {
            errorResponse(request, response, "El número de empleado ya se encuentra registrado.");
            return;
        }

        // 5. Crear objeto Usuario e insertar en base de datos
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellidoPaterno(apellidoPaterno);
        nuevoUsuario.setApellidoMaterno(apellidoMaterno);
        nuevoUsuario.setRol(rol);
        nuevoUsuario.setIdDivision(idDivision);
        nuevoUsuario.setNumeroEmpleado(numeroEmpleado);
        nuevoUsuario.setTelefono(telefono);
        nuevoUsuario.setCorreoInstitucional(correoInstitucional);
        nuevoUsuario.setContrasena(contra); // A futuro se hasheará con BCrypt
        nuevoUsuario.setCreadoPor(null); // Registro autónomo

        boolean registrado = usuarioDao.registrarUsuario(nuevoUsuario);

        if (registrado) {
            request.setAttribute("mensajeExito", "Tu cuenta se ha creado exitosamente. Ya puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            errorResponse(request, response, "Ocurrió un error al guardar tu cuenta en la base de datos. Intenta más tarde.");
        }
    }

    private void errorResponse(HttpServletRequest request, HttpServletResponse response, String mensaje) throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        // Regresar valores al formulario para que el usuario no tenga que reescribir todo
        request.setAttribute("nombre", request.getParameter("nombre"));
        request.setAttribute("apellidoPaterno", request.getParameter("apellidoPaterno"));
        request.setAttribute("apellidoMaterno", request.getParameter("apellidoMaterno"));
        request.setAttribute("numeroEmpleado", request.getParameter("numeroEmpleado"));
        request.setAttribute("correoInstitucional", request.getParameter("correoInstitucional"));
        request.setAttribute("telefono", request.getParameter("telefono"));
        request.setAttribute("division", request.getParameter("division"));
        request.setAttribute("rol", request.getParameter("rol"));
        
        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }
}
