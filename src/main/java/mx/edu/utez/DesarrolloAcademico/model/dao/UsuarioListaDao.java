package mx.edu.utez.DesarrolloAcademico.model.dao;

import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.model.agregarEvento_co;
import mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioListaDao {

    public List<Usuario> listarPorRoles(String... roles) {
        List<Usuario> lista = new ArrayList<>();
        if (roles == null || roles.length == 0) return lista;

        StringBuilder sb = new StringBuilder("SELECT id_usuario, nombre, apellido_paterno, apellido_materno, correo_institucional, numero_empleado, id_division, telefono, activo, rol FROM usuarios WHERE LOWER(rol) IN (");
        for (int i = 0; i < roles.length; i++) {
            sb.append(i == 0 ? "?" : ",?");
        }
        sb.append(") ORDER BY nombre");

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sb.toString())) {
            for (int i = 0; i < roles.length; i++) {
                ps.setString(i + 1, roles[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellidoPaterno(rs.getString("apellido_paterno"));
                    u.setApellidoMaterno(rs.getString("apellido_materno"));
                    u.setCorreoInstitucional(rs.getString("correo_institucional"));
                    u.setNumeroEmpleado(rs.getString("numero_empleado"));
                    u.setTelefono(rs.getString("telefono"));
                    Object divObj = rs.getObject("id_division");
                    u.setIdDivision(divObj != null ? ((Number) divObj).intValue() : null);
                    u.setActivo(rs.getInt("activo"));
                    u.setRol(rs.getString("rol"));
                    lista.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<agregarEvento_co> listarEventosPorUsuario(int idUsuario) {
        List<agregarEvento_co> lista = new ArrayList<>();
        String query = "SELECT e.id_evento, e.nombre, e.lugar, e.institucion, e.tipo_evento, e.descripcion, e.fecha_inicio, e.fecha_fin, e.modalidad " +
                "FROM eventos e " +
                "JOIN participantes_eventos pe ON e.id_evento = pe.id_evento " +
                "WHERE pe.id_usuario = ? ORDER BY e.fecha_inicio DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    agregarEvento_co ev = new agregarEvento_co();
                    ev.setId(rs.getInt("id_evento"));
                    ev.setNombre(rs.getString("nombre"));
                    ev.setLugar(rs.getString("lugar"));
                    ev.setInstitucion(rs.getString("institucion"));
                    ev.setTipo(rs.getString("tipo_evento"));
                    ev.setDescripcion(rs.getString("descripcion"));
                    Timestamp tsInicio = rs.getTimestamp("fecha_inicio");
                    Timestamp tsFin = rs.getTimestamp("fecha_fin");
                    ev.setFechaInicio(tsInicio != null ? tsInicio.toLocalDateTime().toLocalDate().toString() : "");
                    ev.setFechaFin(tsFin != null ? tsFin.toLocalDateTime().toLocalDate().toString() : "");
                    ev.setModalidad(rs.getString("modalidad"));
                    lista.add(ev);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Usuario> listarParticipantesPorEvento(int idEvento) {
        List<Usuario> lista = new ArrayList<>();
        String query = "SELECT u.id_usuario, u.nombre, u.apellido_paterno, u.apellido_materno, u.correo_institucional, u.numero_empleado, u.activo, u.rol " +
                "FROM usuarios u " +
                "JOIN participantes_eventos pe ON u.id_usuario = pe.id_usuario " +
                "WHERE pe.id_evento = ? ORDER BY u.nombre";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idEvento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellidoPaterno(rs.getString("apellido_paterno"));
                    u.setApellidoMaterno(rs.getString("apellido_materno"));
                    u.setCorreoInstitucional(rs.getString("correo_institucional"));
                    u.setNumeroEmpleado(rs.getString("numero_empleado"));
                    u.setActivo(rs.getInt("activo"));
                    u.setRol(rs.getString("rol"));
                    lista.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean asignarParticipante(int idEvento, int idUsuario, int idRegistrador) {
        String query = "INSERT INTO participantes_eventos (id_evento, id_usuario, registrado_por, fecha_registro) VALUES (?, ?, ?, SYSDATE)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idEvento);
            ps.setInt(2, idUsuario);
            ps.setInt(3, idRegistrador);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removerParticipante(int idEvento, int idUsuario) {
        String query = "DELETE FROM participantes_eventos WHERE id_evento = ? AND id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idEvento);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarUsuario(Usuario u) {
        boolean estado = false;
        String query = "UPDATE usuarios SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, id_division = ?, numero_empleado = ?, telefono = ?, correo_institucional = ? WHERE id_usuario = ?";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellidoPaterno());
            ps.setString(3, u.getApellidoMaterno());
            if (u.getIdDivision() != null) {
                ps.setInt(4, u.getIdDivision());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setString(5, u.getNumeroEmpleado());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getCorreoInstitucional());
            ps.setInt(8, u.getIdUsuario());
            
            int filas = ps.executeUpdate();
            estado = (filas > 0);
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return estado;
    }
}
