package mx.edu.utez.DesarrolloAcademico.model.dao;

import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDao {

    // Método auxiliar para mapear el ResultSet al objeto Usuario
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellidoPaterno(rs.getString("apellido_paterno"));
        usuario.setApellidoMaterno(rs.getString("apellido_materno"));
        usuario.setRol(rs.getString("rol"));
        
        int idDivision = rs.getInt("id_division");
        if (!rs.wasNull()) {
            usuario.setIdDivision(idDivision);
        }
        
        usuario.setNumeroEmpleado(rs.getString("numero_empleado"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setCorreoInstitucional(rs.getString("correo_institucional"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        usuario.setActivo(rs.getInt("activo"));
        
        int creadoPor = rs.getInt("creado_por");
        if (!rs.wasNull()) {
            usuario.setCreadoPor(creadoPor);
        }
        
        return usuario;
    }

    public Usuario login(String credencial, String contrasena) {
        Usuario usuario = null;
        // Permite loguearse con correo o con número de empleado
        String query = "SELECT * FROM usuarios WHERE (correo_institucional = ? OR numero_empleado = ?) AND contrasena = ? AND activo = 1";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setString(1, credencial);
            ps.setString(2, credencial);
            ps.setString(3, contrasena); // TODO: A futuro, aquí se consultaría por usuario y luego se usaría BCrypt.checkpw
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public Usuario buscarPorEmailOEmpleado(String credencial) {
        Usuario usuario = null;
        String query = "SELECT * FROM usuarios WHERE correo_institucional = ? OR numero_empleado = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setString(1, credencial);
            ps.setString(2, credencial);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public boolean guardarCodigoRecuperacion(int idUsuario, String codigo) {
        // En Oracle, sumar 15 minutos se hace con INTERVAL
        String query = "INSERT INTO tokens_recuperacion (id_usuario, codigo_token, fecha_expiracion) VALUES (?, ?, CURRENT_TIMESTAMP + INTERVAL '15' MINUTE)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setInt(1, idUsuario);
            ps.setString(2, codigo);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Usuario verificarCodigo(String codigo) {
        Usuario usuario = null;
        String query = "SELECT u.* FROM usuarios u " +
                       "JOIN tokens_recuperacion t ON u.id_usuario = t.id_usuario " +
                       "WHERE t.codigo_token = ? AND t.utilizado = 0 AND t.fecha_expiracion > CURRENT_TIMESTAMP";
                       
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setString(1, codigo);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public boolean actualizarPasswordLimpiaCodigo(int idUsuario, String nuevaPassword) {
        Connection con = null;
        boolean exitoso = false;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false); // Iniciar transacción

            // 1. Actualizar contraseña
            String queryUpdatePass = "UPDATE usuarios SET contrasena = ? WHERE id_usuario = ?";
            try (PreparedStatement ps1 = con.prepareStatement(queryUpdatePass)) {
                ps1.setString(1, nuevaPassword);
                ps1.setInt(2, idUsuario);
                ps1.executeUpdate();
            }

            // 2. Marcar todos los tokens de ese usuario como utilizados
            String queryUpdateToken = "UPDATE tokens_recuperacion SET utilizado = 1 WHERE id_usuario = ?";
            try (PreparedStatement ps2 = con.prepareStatement(queryUpdateToken)) {
                ps2.setInt(1, idUsuario);
                ps2.executeUpdate();
            }

            con.commit();
            exitoso = true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Deshacer cambios si hay error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return exitoso;
    }

    public boolean registrarUsuario(Usuario usuario) {
        boolean estado = false;
        String query = "INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, rol, id_division, numero_empleado, telefono, correo_institucional, contrasena, fecha_registro, activo, creado_por) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, 1, ?)";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidoPaterno());
            ps.setString(3, usuario.getApellidoMaterno());
            ps.setString(4, usuario.getRol());
            
            if (usuario.getIdDivision() != null) {
                ps.setInt(5, usuario.getIdDivision());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            
            ps.setString(6, usuario.getNumeroEmpleado());
            ps.setString(7, usuario.getTelefono());
            ps.setString(8, usuario.getCorreoInstitucional());
            ps.setString(9, usuario.getContrasena());
            
            if (usuario.getCreadoPor() != null) {
                ps.setInt(10, usuario.getCreadoPor());
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }
            
            estado = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estado;
    }

    public boolean actualizarPasswordEnCuenta(int idUsuario, String actualPassword, String nuevaPassword) {
        String query = "UPDATE usuarios SET contrasena = ? WHERE id_usuario = ? AND contrasena = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setString(1, nuevaPassword);
            ps.setInt(2, idUsuario);
            ps.setString(3, actualPassword);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
