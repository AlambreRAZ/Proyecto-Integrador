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
        
        request.setAttribute("mensajeInfo", "Si el correo o número de empleado se encuentra registrado, te llegará un correo electrónico con instrucciones.");
        request.setAttribute("step", "verificar");
        request.getRequestDispatcher("recuperar-contra.jsp").forward(request, response);
    }

    private void verificarCodigo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codigo = request.getParameter("codigo");
        
        Usuario usuario = usuarioDao.verificarCodigo(codigo);
        
        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("idUsuarioRecuperacion", usuario.getIdUsuario());
            session.setAttribute("emailUsuarioRecuperacion", usuario.getCorreoInstitucional());
            
            request.setAttribute("step", "cambiar");
            request.getRequestDispatcher("recuperar-contra.jsp").forward(request, response);
        } else {
            request.setAttribute("mensajeError", "Código incorrecto o expirado, intenta de nuevo.");
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

        if (pass1 != null && pass1.equals(pass2) && !pass1.trim().isEmpty()) {
            boolean actualizado = usuarioDao.actualizarPasswordLimpiaCodigo(idUsuario, pass1);
            
            if (actualizado) {
                emailService.enviarConfirmacionCambio(email);
                
                session.removeAttribute("idUsuarioRecuperacion");
                session.removeAttribute("emailUsuarioRecuperacion");
                
                request.setAttribute("mensajeExito", "Tu contraseña ha sido cambiada exitosamente. Ya puedes iniciar sesión.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                request.setAttribute("mensajeError", "Error al actualizar la contraseña en la BD.");
                request.setAttribute("step", "cambiar");
                request.getRequestDispatcher("recuperar-contra.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("mensajeError", "Las contraseñas no coinciden o están vacías.");
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
