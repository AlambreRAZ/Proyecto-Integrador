<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection" %>
<%
    out.println("=== USUARIOS EN BD ===");
    try {
        Connection con = DatabaseConnection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id_usuario, nombre, apellido_paterno, rol, activo, id_division FROM usuarios");
        while(rs.next()) {
            out.println("ID:" + rs.getInt("id_usuario") + " | Nom:" + rs.getString("nombre") + " " + rs.getString("apellido_paterno") + " | Rol:'" + rs.getString("rol") + "' | Activo:" + rs.getInt("activo"));
        }
        rs.close();
        stmt.close();
        con.close();
    } catch(Exception e) {
        out.println("ERROR: " + e.getMessage());
    }
    
    out.println("=== PARTICIPANTES EN EVENTOS ===");
    try {
        Connection con = DatabaseConnection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id_evento, id_usuario FROM participantes_eventos");
        while(rs.next()) {
            out.println("Evento:" + rs.getInt("id_evento") + " | Usuario:" + rs.getInt("id_usuario"));
        }
        rs.close();
        stmt.close();
        con.close();
    } catch(Exception e) {
        out.println("ERROR: " + e.getMessage());
    }
%>
