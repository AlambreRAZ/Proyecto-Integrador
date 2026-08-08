package mx.edu.utez.DesarrolloAcademico.controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection;


public class ConstanciaDao {





    public boolean verificarConstanciaExistente(int idParticipante) {
        String sql = "SELECT COUNT(*) FROM constancias WHERE id_participante = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idParticipante);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean guardarConstancia(int idParticipante, String rutaArchivo, String nombreArchivo, boolean tieneVigencia, String fechaVencimiento, int subidoPor) {
        if (idParticipante <= 0) return false;

        String query;
        if (tieneVigencia && fechaVencimiento != null && !fechaVencimiento.trim().isEmpty()) {
            query = "INSERT INTO constancias (id_participante, ruta_archivo, nombre_archivo, tiene_vigencia, fecha_vencimiento, subido_por) " +
                    "VALUES (?, ?, ?, 1, TO_DATE(?, 'YYYY-MM-DD'), ?)";
        } else {
            query = "INSERT INTO constancias (id_participante, ruta_archivo, nombre_archivo, tiene_vigencia, subido_por) " +
                    "VALUES (?, ?, ?, 0, ?)";
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idParticipante);
            ps.setString(2, rutaArchivo);
            ps.setString(3, nombreArchivo);

            if (tieneVigencia && fechaVencimiento != null && !fechaVencimiento.trim().isEmpty()) {
                ps.setString(4, fechaVencimiento.trim());
                ps.setInt(5, subidoPor);
            } else {
                ps.setInt(4, subidoPor);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL en guardarConstancia: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}