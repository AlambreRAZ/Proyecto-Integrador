package mx.edu.utez.DesarrolloAcademico.model.dao;

import mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO NUEVO.
 * Sirve para validar si la división de un evento tiene un periodo de carga
 * ACTIVO y VIGENTE (la fecha de hoy cae entre fecha_inicio y fecha_fin).
 *
 * Se usa en:
 *   - VerificarPeriodoCargaServlet (para bloquear el formulario en el front)
 *   - CargarArchivo (validación de servidor)
 *   - SubirConstanciaServlet (validación de servidor)
 */
public class PeriodoCargaDao {

    /** Devuelve el ID_DIVISION al que pertenece un evento, o null si no existe. */
    public Integer obtenerDivisionEvento(int idEvento) {
        String sql = "SELECT id_division FROM eventos WHERE id_evento = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEvento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object v = rs.getObject("id_division");
                    return v != null ? ((Number) v).intValue() : null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la división del evento: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /** Nombre de la división (para mensajes bonitos al usuario). */
    public String obtenerNombreDivision(int idDivision) {
        String sql = "SELECT nombre FROM divisiones WHERE id_division = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDivision);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "tu división";
    }

    /**
     * true  -> la división SÍ tiene periodo de carga habilitado hoy  (deja subir evidencia)
     * false -> NO hay periodo habilitado                             (NO deja subir evidencia)
     */
    public boolean hayPeriodoActivo(int idDivision) {
        String sql = "SELECT COUNT(*) FROM periodos_carga " +
                "WHERE id_division = ? " +
                "  AND activo = 1 " +
                "  AND TRUNC(SYSDATE) BETWEEN TRUNC(fecha_inicio) AND TRUNC(fecha_fin)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDivision);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar el periodo de carga: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /** Atajo: valida directamente a partir del evento. */
    public boolean puedeSubirEvidencia(int idEvento) {
        Integer idDivision = obtenerDivisionEvento(idEvento);
        if (idDivision == null) return false;
        return hayPeriodoActivo(idDivision);
    }
}