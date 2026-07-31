package mx.edu.utez.DesarrolloAcademico.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarDesarrollador_Dao;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;



// Regresa en JSON a todos los usuarios con rol 'desarrollo', para la tabla
// de "Gestión de Desarrolladores" (gestion_desarrolladores_de.jsp).
@WebServlet(name = "ListarDesarrollador", value = "/ListarDesarrollador")
public class ListarDesarrollador extends HttpServlet {

    private String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        AgregarDesarrollador_Dao dao = new AgregarDesarrollador_Dao();
        List<Usuario> desarrolladores = dao.listarDesarrolladores();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < desarrolladores.size(); i++) {
            Usuario u = desarrolladores.get(i);
            if (i > 0) json.append(",");
            json.append("{")
                    .append("\"id\":").append(u.getIdUsuario()).append(",")
                    .append("\"nombre\":\"").append(escapar(u.getNombre())).append("\",")
                    .append("\"apellidoPaterno\":\"").append(escapar(u.getApellidoPaterno())).append("\",")
                    .append("\"apellidoMaterno\":\"").append(escapar(u.getApellidoMaterno())).append("\",")
                    .append("\"idDivision\":").append(u.getIdDivision() != null ? u.getIdDivision() : "null").append(",")
                    .append("\"numeroEmpleado\":\"").append(escapar(u.getNumeroEmpleado())).append("\",")
                    .append("\"telefono\":\"").append(escapar(u.getTelefono())).append("\",")
                    .append("\"correo\":\"").append(escapar(u.getCorreoInstitucional())).append("\",")
                    .append("\"activo\":").append(u.getActivo())
                    .append("}");
        }
        json.append("]");

        out.write(json.toString());
        out.flush();
    }
}