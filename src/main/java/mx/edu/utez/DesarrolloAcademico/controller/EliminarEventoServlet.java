package mx.edu.utez.DesarrolloAcademico.controller;

import jakarta.servlet.annotation.MultipartConfig;
import mx.edu.utez.DesarrolloAcademico.model.dao.AgregarEvento_Co;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "EliminarEventoServlet", value = "/EliminarEventoServlet")
@MultipartConfig

public class EliminarEventoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String idStr = request.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\": false, \"message\": \"Id de evento inválido.\"}");
            out.flush();
            return;
        }

        AgregarEvento_Co dao = new AgregarEvento_Co();
        boolean exito = dao.eliminarEvento(id);

        if (exito) {
            out.write("{\"success\": true, \"message\": \"Evento eliminado correctamente.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"No se pudo eliminar el evento.\"}");
        }
        out.flush();
    }

    public boolean eliminarEvento(int idEvento) {
        boolean estado = false;
        Connection con = null;
        PreparedStatement psParticipantes = null;
        PreparedStatement psDocentes = null;
        PreparedStatement psEvento = null;

        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            // Primero se eliminan las tablas hijas (llaves foráneas hacia eventos)
            psParticipantes = con.prepareStatement("DELETE FROM participantes_eventos WHERE id_evento = ?");
            psParticipantes.setInt(1, idEvento);
            psParticipantes.executeUpdate();

            psDocentes = con.prepareStatement("DELETE FROM evento_docente WHERE id_evento = ?");
            psDocentes.setInt(1, idEvento);
            psDocentes.executeUpdate();

            psEvento = con.prepareStatement("DELETE FROM eventos WHERE id_evento = ?");
            psEvento.setInt(1, idEvento);
            int filasAfectadas = psEvento.executeUpdate();

            con.commit();
            estado = filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el evento: " + e.getMessage());
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (psParticipantes != null) psParticipantes.close();
                if (psDocentes != null) psDocentes.close();
                if (psEvento != null) psEvento.close();
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return estado;
    }
}
