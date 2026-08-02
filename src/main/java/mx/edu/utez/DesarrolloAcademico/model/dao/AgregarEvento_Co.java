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

    public List<agregarEvento_co> listarEventos() {
        List<agregarEvento_co> eventos = new ArrayList<>();
        String query = "SELECT id_evento, nombre, lugar, institucion, tipo_evento, descripcion, fecha_inicio, fecha_fin, modalidad FROM eventos ORDER BY id_evento DESC";

        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

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
                eventos.add(evento);
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

        Connection con = DatabaseConnection.getConnection();
        if (con == null) {
            System.err.println("Error al obtener el evento: no se pudo obtener conexión a la base de datos.");
            return null;
        }
        try (PreparedStatement ps = con.prepareStatement(query)) {
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

            String query = "UPDATE eventos SET nombre = ?, lugar = ?, institucion = ?, tipo_evento = ?, descripcion = ?, fecha_inicio = ?, fecha_fin = ?, modalidad = ? WHERE id_evento = ?";
            psEvento = con.prepareStatement(query);

            psEvento.setString(1, evento.getNombre());
            psEvento.setString(2, evento.getLugar());
            psEvento.setString(3, evento.getInstitucion());
            psEvento.setString(4, evento.getTipo());
            psEvento.setString(5, evento.getDescripcion());
            psEvento.setTimestamp(6, java.sql.Timestamp.valueOf(evento.getFechaInicio() + " 00:00:00"));
            psEvento.setTimestamp(7, java.sql.Timestamp.valueOf(evento.getFechaFin() + " 00:00:00"));
            psEvento.setString(8, evento.getModalidad());
            psEvento.setInt(9, evento.getId());

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
}