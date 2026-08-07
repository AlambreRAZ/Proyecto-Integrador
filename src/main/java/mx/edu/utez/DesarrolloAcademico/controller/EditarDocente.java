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
            String idStr = request.getParameter("id");
            String nombre = request.getParameter("nombre");
            String apellidoPaterno = request.getParameter("apellidoPaterno");
            String apellidoMaterno = request.getParameter("apellidoMaterno");
            String divisionStr = request.getParameter("idDivision");
            String numeroEmpleado = request.getParameter("numeroEmpleado");
            String telefono = request.getParameter("telefono");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");
            String confirmarContrasena = request.getParameter("confirmar_contrasena");

            if (idStr == null || nombre == null || apellidoPaterno == null ||
                    divisionStr == null || numeroEmpleado == null || telefono == null || correo == null) {
                out.write("{\"success\": false, \"message\": \"Campos requeridos faltantes.\"}");
                return;
            }

            if (contrasena != null && !contrasena.trim().isEmpty()) {
                if (contrasena.trim().length() < 8) {
                    out.write("{\"success\": false, \"message\": \"La contraseña debe tener al menos 8 caracteres.\"}");
                    return;
                }
                if (!contrasena.equals(confirmarContrasena)) {
                    out.write("{\"success\": false, \"message\": \"Las contraseñas no coinciden.\"}");
                    return;
                }
            } else {
                contrasena = null;
            }

            int id = Integer.parseInt(idStr.trim());
            int idDivision = Integer.parseInt(divisionStr.trim());

            AgregarDesarrollador_Dao dao = new AgregarDesarrollador_Dao();

            Usuario dev = new Usuario();
            dev.setIdUsuario(id);
            dev.setNombre(nombre.trim());
            dev.setApellidoPaterno(apellidoPaterno.trim());
            dev.setApellidoMaterno(apellidoMaterno != null ? apellidoMaterno.trim() : "");
            dev.setIdDivision(idDivision);
            dev.setNumeroEmpleado(numeroEmpleado.trim());
            dev.setTelefono(telefono.trim());
            dev.setCorreoInstitucional(correo.trim());

            boolean actualizado = dao.actualizarDesarrollador(dev, contrasena);

            if (actualizado) {
                out.write("{\"success\": true}");
            } else {
                out.write("{\"success\": false, \"message\": \"No se pudo actualizar en la base de datos.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}");
        }
        out.flush();
    }
}