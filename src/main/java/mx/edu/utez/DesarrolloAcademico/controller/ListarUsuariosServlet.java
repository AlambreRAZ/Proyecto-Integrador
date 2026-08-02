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
import java.util.List;

@WebServlet(name = "ListarUsuariosServlet", value = "/ListarUsuariosServlet")
public class ListarUsuariosServlet extends HttpServlet {

    private final UsuarioListaDao dao = new UsuarioListaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String rol = request.getParameter("rol");
        List<Usuario> usuarios;
        if (rol != null && !rol.isEmpty()) {
            usuarios = dao.listarPorRoles(rol);
        } else {
            usuarios = dao.listarPorRoles("docente", "coordinador");
        }

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            if (i > 0) json.append(",");
            json.append("{")
                .append("\"id\":").append(u.getIdUsuario()).append(",")
                .append("\"nombre\":\"").append(esc(u.getNombre())).append("\",")
                .append("\"apellidoPaterno\":\"").append(esc(u.getApellidoPaterno())).append("\",")
                .append("\"apellidoMaterno\":\"").append(esc(u.getApellidoMaterno())).append("\",")
                .append("\"correo\":\"").append(esc(u.getCorreoInstitucional())).append("\",")
                .append("\"numeroEmpleado\":\"").append(esc(u.getNumeroEmpleado())).append("\",")
                .append("\"telefono\":\"").append(esc(u.getTelefono())).append("\",")
                .append("\"idDivision\":").append(u.getIdDivision() != null ? u.getIdDivision() : "null").append(",")
                .append("\"activo\":").append(u.getActivo()).append(",")
                .append("\"rol\":\"").append(esc(u.getRol())).append("\"")
                .append("}");
        }
        json.append("]");

        out.write(json.toString());
        out.flush();
    }

    private String esc(String v) {
        if (v == null) return "";
        return v.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
