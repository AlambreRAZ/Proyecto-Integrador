package mx.edu.utez.DesarrolloAcademico.model.dao;

import mx.edu.utez.DesarrolloAcademico.model.agregarEvento_co;
import mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AgregarEvento_Co {

    public boolean registrarEvento(agregarEvento_co evento) {
        boolean estado = false;
        Connection con = null;
        PreparedStatement psEvento = null;
        PreparedStatement psDocentes = null;
        ResultSet rs = null;

        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            String queryEvento = "INSERT INTO eventos (nombre, lugar, institucion, tipo_evento, descripcion, fecha_inicio, fecha_fin, modalidad, id_division, creado_por, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
            String[] returnId = { "ID_EVENTO" };
            psEvento = con.prepareStatement(queryEvento, returnId);

            psEvento.setString(1, evento.getNombre());
            psEvento.setString(2, evento.getLugar());
            psEvento.setString(3, evento.getInstitucion());
            psEvento.setString(4, evento.getTipo());
            psEvento.setString(5, evento.getDescripcion());
            psEvento.setDate(6, Date.valueOf(LocalDate.parse(evento.getFechaInicio())));
            psEvento.setDate(7, Date.valueOf(LocalDate.parse(evento.getFechaFin())));
            psEvento.setString(8, evento.getModalidad());
            psEvento.setInt(9, evento.getIdDivision());
            psEvento.setInt(10, evento.getCreadoPor());

            int filasAfectadas = psEvento.executeUpdate();

            if (filasAfectadas > 0) {
                rs = psEvento.getGeneratedKeys();
                if (rs.next()) {
                    int idEventoGenerado = rs.getInt(1);

                    if (evento.getDocentesAsignados() != null && !evento.getDocentesAsignados().isEmpty()) {
                        String queryDocentes = "INSERT INTO participantes_eventos (id_evento, id_usuario, registrado_por, fecha_registro) VALUES (?, ?, ?, SYSDATE)";
                        psDocentes = con.prepareStatement(queryDocentes);

                        for (Integer idDocente : evento.getDocentesAsignados()) {
                            psDocentes.setInt(1, idEventoGenerado);
                            psDocentes.setInt(2, idDocente);
                            psDocentes.setInt(3, evento.getCreadoPor());
                            psDocentes.addBatch();
                        }
                        psDocentes.executeBatch();
                    }

                    con.commit();
                    estado = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el evento: " + e.getMessage());
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (psEvento != null) psEvento.close();
                if (psDocentes != null) psDocentes.close();
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return estado;
    }

    public List<agregarEvento_co> listarEventos(Integer idDivision) {
        List<agregarEvento_co> eventos = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT e.id_evento, e.nombre, e.lugar, e.institucion, e.tipo_evento, e.descripcion, e.fecha_inicio, e.fecha_fin, e.modalidad, d.nombre AS nombre_division ");
        query.append("FROM eventos e LEFT JOIN divisiones d ON e.id_division = d.id_division ");

        if (idDivision != null) {
            query.append("WHERE e.id_division = ? ");
        }
        query.append("ORDER BY e.id_evento DESC");

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            if (idDivision != null) {
                ps.setInt(1, idDivision);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    agregarEvento_co evento = new agregarEvento_co();
                    evento.setId(rs.getInt("id_evento"));
                    evento.setNombre(rs.getString("nombre"));
                    evento.setLugar(rs.getString("lugar"));
                    evento.setInstitucion(rs.getString("institucion"));
                    evento.setTipo(rs.getString("tipo_evento"));
                    evento.setDescripcion(rs.getString("descripcion"));
                    java.sql.Timestamp tsInicio = rs.getTimestamp("fecha_inicio");
                    java.sql.Timestamp tsFin = rs.getTimestamp("fecha_fin");
                    evento.setFechaInicio(tsInicio != null ? tsInicio.toLocalDateTime().toLocalDate().toString() : "");
                    evento.setFechaFin(tsFin != null ? tsFin.toLocalDateTime().toLocalDate().toString() : "");
                    evento.setModalidad(rs.getString("modalidad"));
                    evento.setNombreDivision(rs.getString("nombre_division"));
                    eventos.add(evento);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar eventos: " + e.getMessage());
            e.printStackTrace();
        }
        return eventos;
    }

    public boolean eliminarEvento(int idEvento) {
        boolean estado = false;
        Connection con = null;
        PreparedStatement psEvento = null;

        try {
            con = DatabaseConnection.getConnection();
            if (con == null) {
                throw new SQLException("No se pudo obtener conexión a la base de datos.");
            }
            con.setAutoCommit(false);

            psEvento = con.prepareStatement("DELETE FROM eventos WHERE id_evento = ?");
            psEvento.setInt(1, idEvento);
            int filasAfectadas = psEvento.executeUpdate();

            con.commit();
            estado = filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar el evento: " + e.getMessage());
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (psEvento != null) psEvento.close();
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return estado;
    }
    public agregarEvento_co obtenerPorId(int idEvento) {
        String query = "SELECT id_evento, nombre, lugar, institucion, tipo_evento, descripcion, fecha_inicio, fecha_fin, modalidad, id_division, creado_por FROM eventos WHERE id_evento = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idEvento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    agregarEvento_co evento = new agregarEvento_co();
                    evento.setId(rs.getInt("id_evento"));
                    evento.setNombre(rs.getString("nombre"));
                    evento.setLugar(rs.getString("lugar"));
                    evento.setInstitucion(rs.getString("institucion"));
                    evento.setTipo(rs.getString("tipo_evento"));
                    evento.setDescripcion(rs.getString("descripcion"));
                    java.sql.Timestamp tsInicio = rs.getTimestamp("fecha_inicio");
                    java.sql.Timestamp tsFin = rs.getTimestamp("fecha_fin");
                    evento.setFechaInicio(tsInicio != null ? tsInicio.toLocalDateTime().toLocalDate().toString() : "");
                    evento.setFechaFin(tsFin != null ? tsFin.toLocalDateTime().toLocalDate().toString() : "");
                    evento.setModalidad(rs.getString("modalidad"));
                    evento.setIdDivision(rs.getInt("id_division"));
                    evento.setCreadoPor(rs.getInt("creado_por"));
                    return evento;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener el evento: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizarEvento(agregarEvento_co evento) {
        boolean estado = false;
        Connection con = null;
        PreparedStatement psEvento = null;

        try {
            con = DatabaseConnection.getConnection();
            if (con == null) {
                throw new SQLException("No se pudo obtener conexión a la base de datos.");
            }
            con.setAutoCommit(false);

            // CORREGIDO: ahora tambien se actualiza id_division
            String query = "UPDATE eventos SET nombre = ?, lugar = ?, institucion = ?, tipo_evento = ?, " +
                    "descripcion = ?, fecha_inicio = ?, fecha_fin = ?, modalidad = ?, id_division = ? " +
                    "WHERE id_evento = ?";
            psEvento = con.prepareStatement(query);

            psEvento.setString(1, evento.getNombre());
            psEvento.setString(2, evento.getLugar());
            psEvento.setString(3, evento.getInstitucion());
            psEvento.setString(4, evento.getTipo());
            psEvento.setString(5, evento.getDescripcion());
            psEvento.setTimestamp(6, java.sql.Timestamp.valueOf(evento.getFechaInicio() + " 00:00:00"));
            psEvento.setTimestamp(7, java.sql.Timestamp.valueOf(evento.getFechaFin() + " 00:00:00"));
            psEvento.setString(8, evento.getModalidad());
            psEvento.setInt(9, evento.getIdDivision());
            psEvento.setInt(10, evento.getId());

            int filasAfectadas = psEvento.executeUpdate();

            con.commit();
            estado = filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al actualizar el evento: " + e.getMessage());
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (psEvento != null) psEvento.close();
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return estado;
    }

    /**
     * METODO NUEVO.
     * Sincroniza la lista de docentes asignados a un evento:
     *   - borra los participantes que ya no vienen en la lista
     *   - inserta los que son nuevos
     *   - deja intactos los que ya estaban (no se pierden sus constancias)
     *
     * Si la lista viene null, no toca nada (util cuando el formulario
     * de edicion no manda docentes).
     */
    public boolean actualizarDocentesEvento(int idEvento, List<Integer> idsDocentes) {
        if (idsDocentes == null) return true;

        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            if (con == null) {
                throw new SQLException("No se pudo obtener conexion a la base de datos.");
            }
            con.setAutoCommit(false);

            // 1) Quien registra: usamos el creador del evento
            int registradoPor = 0;
            try (PreparedStatement psC = con.prepareStatement(
                    "SELECT creado_por FROM eventos WHERE id_evento = ?")) {
                psC.setInt(1, idEvento);
                try (ResultSet rs = psC.executeQuery()) {
                    if (rs.next()) registradoPor = rs.getInt("creado_por");
                }
            }

            // 2) Participantes que ya estan en la BD
            List<Integer> actuales = new ArrayList<>();
            try (PreparedStatement psA = con.prepareStatement(
                    "SELECT id_usuario FROM participantes_eventos WHERE id_evento = ?")) {
                psA.setInt(1, idEvento);
                try (ResultSet rs = psA.executeQuery()) {
                    while (rs.next()) actuales.add(rs.getInt("id_usuario"));
                }
            }

            // 3) Borrar los que ya no vienen en la lista
            try (PreparedStatement psD = con.prepareStatement(
                    "DELETE FROM participantes_eventos WHERE id_evento = ? AND id_usuario = ?")) {
                for (Integer actual : actuales) {
                    if (!idsDocentes.contains(actual)) {
                        psD.setInt(1, idEvento);
                        psD.setInt(2, actual);
                        psD.addBatch();
                    }
                }
                psD.executeBatch();
            }

            // 4) Insertar los nuevos
            try (PreparedStatement psI = con.prepareStatement(
                    "INSERT INTO participantes_eventos (id_evento, id_usuario, registrado_por, fecha_registro) " +
                            "VALUES (?, ?, ?, SYSDATE)")) {
                for (Integer nuevoId : idsDocentes) {
                    if (!actuales.contains(nuevoId)) {
                        psI.setInt(1, idEvento);
                        psI.setInt(2, nuevoId);
                        psI.setInt(3, registradoPor);
                        psI.addBatch();
                    }
                }
                psI.executeBatch();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar docentes del evento: " + e.getMessage());
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try {
                if (con != null) { con.setAutoCommit(true); con.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}