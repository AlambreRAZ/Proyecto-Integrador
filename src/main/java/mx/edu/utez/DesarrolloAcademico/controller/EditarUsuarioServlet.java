package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.UsuarioListaDao;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "EditarUsuarioServlet", value = "/EditarUsuarioServlet")
public class EditarUsuarioServlet extends HttpServlet {

    private final UsuarioListaDao dao = new UsuarioListaDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int idUsuario = Integer.parseInt(request.getParameter("id_usuario"));
            String nombre = request.getParameter("nombre");
            String apellidoP = request.getParameter("apellido_paterno");
            String apellidoM = request.getParameter("apellido_materno");
            String numEmpleado = request.getParameter("numero_empleado");
            String telefono = request.getParameter("telefono");
            String correo = request.getParameter("correo");
            
            String idDivisionStr = request.getParameter("division");
            Integer idDivision = null;
            if (idDivisionStr != null && !idDivisionStr.isEmpty()) {
                idDivision = Integer.parseInt(idDivisionStr);
            }

            Usuario u = new Usuario();
            u.setIdUsuario(idUsuario);
            u.setNombre(nombre);
            u.setApellidoPaterno(apellidoP);
            u.setApellidoMaterno(apellidoM);
            u.setNumeroEmpleado(numEmpleado);
            u.setTelefono(telefono);
            u.setCorreoInstitucional(correo);
            u.setIdDivision(idDivision);

            boolean exito = dao.actualizarUsuario(u);

            if (exito) {
                out.write("{\"success\": true, \"message\": \"Usuario actualizado exitosamente.\"}");
            } else {
                out.write("{\"success\": false, \"message\": \"No se pudo actualizar el usuario en la BD.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Error al procesar la solicitud: " + e.getMessage() + "\"}");
        }
        
        out.flush();
    }
}
