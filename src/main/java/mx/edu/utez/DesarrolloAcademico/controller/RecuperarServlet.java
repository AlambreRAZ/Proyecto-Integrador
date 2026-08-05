package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioDao;
import mx.edu.utez.DesarrolloAcademico.utils.EmailService;

import java.io.IOException;
import java.security.SecureRandom;

@WebServlet(name = "RecuperarServlet", urlPatterns = {"/recuperar"})
public class RecuperarServlet extends HttpServlet {

    private UsuarioDao usuarioDao = new UsuarioDao();
    private EmailService emailService = new EmailService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "solicitar";

        switch (action) {
            case "solicitar":
                solicitarCodigo(request, response);
                break;
            case "verificar":
                verificarCodigo(request, response);
                break;
            case "cambiar":
                cambiarPassword(request, response);
                break;
            default:
                response.sendRedirect("recuperar-contra.jsp");
        }
    }

    private void solicitarCodigo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dato = request.getParameter("dato");

        Usuario usuario = usuarioDao.buscarPorEmailOEmpleado(dato);

        if (usuario != null) {
            String codigo = generarCodigo(6);
            boolean guardado = usuarioDao.guardarCodigoRecuperacion(usuario.getIdUsuario(), codigo);
            if (guardado) {
                emailService.enviarCodigoRecuperacion(usuario.getCorreoInstitucional(), codigo);
            }
        }

        // Guardamos el dato en sesión para poder reenviar el código sin pedir el correo otra vez
        request.getSession().setAttribute("datoRecuperacion", dato);

        request.setAttribute("mensajeInfo", "Si el correo o número de empleado se encuentra registrado, te llegará un correo con instrucciones.");
        request.setAttribute("emailParaReenvio", dato); // Para el botón 'Reenviar' en la vista
        request.setAttribute("step", "verificar");
        request.getRequestDispatcher("recuperar-contra.jsp").forward(request, response);
    }

    private void verificarCodigo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // El nuevo JSP manda el código ensamblado en el campo 'codigoCompleto' (hidden)
        // pero mantenemos compatibilidad con el campo 'codigo' por si acaso
        String codigo = request.getParameter("codigoCompleto");
        if (codigo == null || codigo.trim().isEmpty()) {
            codigo = request.getParameter("codigo");
        }
        if (codigo != null) {
            codigo = codigo.trim().toUpperCase();
        }

        // Recuperamos el dato de sesión para el botón reenviar
        String datoSesion = (String) request.getSession().getAttribute("datoRecuperacion");

        Usuario usuario = usuarioDao.verificarCodigo(codigo);

        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("idUsuarioRecuperacion", usuario.getIdUsuario());
            session.setAttribute("emailUsuarioRecuperacion", usuario.getCorreoInstitucional());

            request.setAttribute("step", "cambiar");
            request.getRequestDispatcher("recuperar-contra.jsp").forward(request, response);
        } else {
            request.setAttribute("mensajeError", "Código incorrecto o expirado, intenta de nuevo.");
            request.setAttribute("emailParaReenvio", datoSesion);
            request.setAttribute("step", "verificar");
            request.getRequestDispatcher("recuperar-contra.jsp").forward(request, response);
        }
    }

    private void cambiarPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer idUsuario = (Integer) session.getAttribute("idUsuarioRecuperacion");
        String email = (String) session.getAttribute("emailUsuarioRecuperacion");
        
        if (idUsuario == null) {
            response.sendRedirect("recuperar-contra.jsp");
            return;
        }

        String pass1 = request.getParameter("pass1");
        String pass2 = request.getParameter("pass2");

        // Validar longitud 12-15 caracteres
        if (pass1 == null || pass1.trim().isEmpty()) {
            request.setAttribute("mensajeError", "La contraseña no puede estar vacía.");
            request.setAttribute("step", "cambiar");
            request.getRequestDispatcher("recuperar-contra.jsp").forward(request, response);
            return;
        }
        if (pass1.length() < 12 || pass1.length() > 15) {
            request.setAttribute("mensajeError", "La contraseña debe tener entre 12 y 15 caracteres.");
            request.setAttribute("step", "cambiar");
            request.getRequestDispatcher("recuperar-contra.jsp").forward(request, response);
            return;
        }
        if (!pass1.equals(pass2)) {
            request.setAttribute("mensajeError", "Las contraseñas no coinciden.");
            request.setAttribute("step", "cambiar");
            request.getRequestDispatcher("recuperar-contra.jsp").forward(request, response);
            return;
        }

        boolean actualizado = usuarioDao.actualizarPasswordLimpiaCodigo(idUsuario, pass1);

        if (actualizado) {
            emailService.enviarConfirmacionCambio(email);
            session.removeAttribute("idUsuarioRecuperacion");
            session.removeAttribute("emailUsuarioRecuperacion");
            request.setAttribute("mensajeExito", "Tu contraseña ha sido cambiada exitosamente. Ya puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("mensajeError", "Error al actualizar la contraseña en la BD.");
            request.setAttribute("step", "cambiar");
            request.getRequestDispatcher("recuperar-contra.jsp").forward(request, response);
        }
    }

    private String generarCodigo(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(longitud);
        for(int i = 0; i < longitud; i++)
            sb.append(caracteres.charAt(rnd.nextInt(caracteres.length())));
        return sb.toString();
    }
}
