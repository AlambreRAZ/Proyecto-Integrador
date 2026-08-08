package mx.edu.utez.DesarrolloAcademico.model.dao;

import mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConstanciaDao {

    // Método para obtener el id_participante en base al idEvento y el idUsuario
    public int obtenerIdParticipante(int idEvento, int idUsuario) {
        int idParticipante = -1;
        String query = "SELECT id_participante FROM participantes_eventos WHERE id_evento = ? AND id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idEvento);
            ps.setInt(2, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idParticipante = rs.getInt("id_participante");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idParticipante;
    }

    // Método para verificar si ya subió una constancia
    public boolean verificarConstanciaExistente(int idParticipante) {
        boolean existe = false;
        String query = "SELECT id_constancia FROM constancias WHERE id_participante = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idParticipante);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    existe = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

    // Método para guardar una constancia en la BD
    public boolean guardarConstancia(int idParticipante, String rutaArchivo, String nombreArchivo, boolean tieneVigencia, String fechaVencimiento, int subidoPor) {
        String query;
        if (tieneVigencia && fechaVencimiento != null && !fechaVencimiento.isEmpty()) {
            query = "INSERT INTO constancias (id_participante, ruta_archivo, nombre_archivo, tiene_vigencia, fecha_vencimiento, subido_por) " +
                    "VALUES (?, ?, ?, 1, TO_TIMESTAMP(?, 'YYYY-MM-DD'), ?)";
        } else {
            query = "INSERT INTO constancias (id_participante, ruta_archivo, nombre_archivo, tiene_vigencia, subido_por) " +
                    "VALUES (?, ?, ?, 0, ?)";
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setInt(1, idParticipante);
            ps.setString(2, rutaArchivo);
            ps.setString(3, nombreArchivo);
            
            if (tieneVigencia && fechaVencimiento != null && !fechaVencimiento.isEmpty()) {
                ps.setString(4, fechaVencimiento);
                ps.setInt(5, subidoPor);
            } else {
                ps.setInt(4, subidoPor);
            }
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean guardarConstanciaCO(int idParticipante, String rutaArchivo, String nombreArchivo, boolean tieneVigencia, String fechaVencimiento, int subidoPor) {

        // Si idParticipante no es válido, no intentamos el INSERT para evitar la excepción de FK
        if (idParticipante <= 0) {
            System.err.println("Error: idParticipante no es válido (" + idParticipante + ")");
            return false;
        }

        String query;
        if (tieneVigencia && fechaVencimiento != null && !fechaVencimiento.trim().isEmpty()) {
            query = "INSERT INTO constancias (id_participante, ruta_archivo, nombre_archivo, tiene_vigencia, fecha_vencimiento, subido_por) " +
                    "VALUES (?, ?, ?, 1, ?, ?)";
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
                // Convertimos la cadena "YYYY-MM-DD" directamente a java.sql.Date (Estándar JDBC)
                ps.setDate(4, java.sql.Date.valueOf(fechaVencimiento.trim()));
                ps.setInt(5, subidoPor);
            } else {
                ps.setInt(4, subidoPor);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Error SQL al guardar constancia:");
            e.printStackTrace();
        }
        return false;
    }

    // Obtiene la constancia de un participante como un mapa de datos
    public Map<String, Object> obtenerConstancia(int idParticipante) {
        Map<String, Object> datos = null;
        String query = "SELECT id_constancia, nombre_archivo, ruta_archivo, tiene_vigencia, fecha_vencimiento, fecha_subida " +
                       "FROM constancias WHERE id_participante = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idParticipante);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    datos = new HashMap<>();
                    datos.put("idConstancia", rs.getInt("id_constancia"));
                    datos.put("nombreArchivo", rs.getString("nombre_archivo"));
                    datos.put("rutaArchivo", rs.getString("ruta_archivo"));
                    datos.put("tieneVigencia", rs.getInt("tiene_vigencia"));
                    datos.put("fechaVencimiento", rs.getTimestamp("fecha_vencimiento"));
                    datos.put("fechaSubida", rs.getTimestamp("fecha_subida"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }

    // Elimina la constancia y devuelve la ruta del archivo para borrarlo del disco
    public String eliminarConstancia(int idConstancia, int idParticipante) {
        String ruta = null;
        // Primero obtenemos la ruta del archivo
        String querySelect = "SELECT ruta_archivo FROM constancias WHERE id_constancia = ? AND id_participante = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(querySelect)) {
            ps.setInt(1, idConstancia);
            ps.setInt(2, idParticipante);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ruta = rs.getString("ruta_archivo");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (ruta == null) return null;

        // Luego eliminamos el registro
        String queryDelete = "DELETE FROM constancias WHERE id_constancia = ? AND id_participante = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(queryDelete)) {
            ps.setInt(1, idConstancia);
            ps.setInt(2, idParticipante);
            if (ps.executeUpdate() > 0) {
                return ruta; // devuelve la ruta para que el servlet borre el archivo
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
