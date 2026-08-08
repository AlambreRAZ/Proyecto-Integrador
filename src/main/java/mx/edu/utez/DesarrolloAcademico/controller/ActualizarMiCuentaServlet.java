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

@WebServlet(name = "ActualizarMiCuentaServlet", value = "/ActualizarMiCuentaServlet")
public class ActualizarMiCuentaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 1. Validar que la sesión exista y que el usuario esté autenticado
        HttpSession session = request.getSession(false);
        Usuario usuarioSesion = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuarioSesion == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"success\": false, \"message\": \"Sesión expirada o no válida.\"}");
            out.flush();
            return;
        }

        // 2. Obtener parámetros del formulario
        String telefono = request.getParameter("telefono");
        String passActual = request.getParameter("passActual");
        String passNueva = request.getParameter("passNueva");

        // Sanitización básica
        telefono = (telefono != null) ? telefono.trim() : "";
        passActual = (passActual != null) ? passActual.trim() : "";
        passNueva = (passNueva != null) ? passNueva.trim() : "";

        // 3. Validación del lado del Servidor: Teléfono (10 dígitos)
        if (!telefono.matches("^\\d{10}$")) {
            out.write("{\"success\": false, \"message\": \"El teléfono debe contener exactamente 10 dígitos numéricos.\"}");
            out.flush();
            return;
        }

        UsuarioDao usuarioDao = new UsuarioDao();
        boolean cambioExitoso = false;
        String mensajeError = "No se pudieron actualizar los datos.";

        // 4. Lógica de Actualización
        if (!passNueva.isEmpty()) {
            // A) El usuario intenta cambiar teléfono Y contraseña

            // Validar longitud de la nueva contraseña (12 a 15 caracteres)
            if (passNueva.length() < 12 || passNueva.length() > 15) {
                out.write("{\"success\": false, \"message\": \"La nueva contraseña debe contener entre 12 y 15 caracteres.\"}");
                out.flush();
                return;
            }

            // Validar que la contraseña actual proporcionada coincida con la de la base de datos / sesión
            if (passActual.isEmpty() || !usuarioSesion.getContrasena().equals(passActual)) {
                out.write("{\"success\": false, \"message\": \"La contraseña actual es incorrecta.\"}");
                out.flush();
                return;
            }

            // Actualizar perfil (Teléfono + Nueva Contraseña)
            cambioExitoso = usuarioDao.actualizarPerfil(usuarioSesion.getIdUsuario(), telefono, passNueva);

            if (cambioExitoso) {
                // Actualizar la sesión con los nuevos valores
                usuarioSesion.setTelefono(telefono);
                usuarioSesion.setContrasena(passNueva);
                session.setAttribute("usuario", usuarioSesion);
            } else {
                mensajeError = "Ocurrió un error en la base de datos al actualizar la contraseña y teléfono.";
            }

        } else {
            // B) El usuario SOLO quiere actualizar su teléfono
            cambioExitoso = usuarioDao.actualizarPerfil(usuarioSesion.getIdUsuario(), telefono, null);

            if (cambioExitoso) {
                // Actualizar la sesión con el nuevo teléfono
                usuarioSesion.setTelefono(telefono);
                session.setAttribute("usuario", usuarioSesion);
            } else {
                mensajeError = "Ocurrió un error en la base de datos al actualizar el teléfono.";
            }
        }

        // 5. Respuesta JSON final
        if (cambioExitoso) {
            out.write("{\"success\": true, \"message\": \"Perfil actualizado correctamente.\"}");
        } else {
            out.write("{\"success\": false, \"message\": \"" + mensajeError + "\"}");
        }

        out.flush();
    }
}