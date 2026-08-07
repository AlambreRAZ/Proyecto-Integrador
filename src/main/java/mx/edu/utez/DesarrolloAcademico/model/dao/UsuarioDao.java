package mx.edu.utez.DesarrolloAcademico.model.dao;

import mx.edu.utez.DesarrolloAcademico.model.Evento;
import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public Usuario buscarPorId(int idUsuario) {
        Usuario usuario = null;
        String query = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en buscarPorId: " + e.getMessage());
            e.printStackTrace();
        }
        return usuario;
    }

    public Usuario login(String credencial, String contrasena) {
        Usuario usuario = null;
        String query = "SELECT * FROM usuarios WHERE (correo_institucional = ? OR numero_empleado = ?) AND contrasena = ? AND activo = 1";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, credencial);
            ps.setString(2, credencial);
            ps.setString(3, contrasena);

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
        // Incluye UTILIZADO = 0 explícitamente para cumplir con la restricción NOT NULL de la tabla Oracle
        String query = "INSERT INTO tokens_recuperacion (id_usuario, codigo_token, utilizado, fecha_expiracion) " +
                "VALUES (?, ?, 0, CURRENT_TIMESTAMP + INTERVAL '15' MINUTE)";
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
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close(); // Cierra la conexión
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

    public List<Evento> obtenerProximosEventos() {
        List<Evento> lista = new ArrayList<>();
        // Consultamos nombre, fechas e ID_EVENTO para el enlace
        String sql = "SELECT ID_EVENTO, NOMBRE, FECHA_INICIO, FECHA_FIN FROM EVENTOS ORDER BY FECHA_INICIO ASC";

        try (Connection con = DatabaseConnection.getConnection(); // Ajusta a tu conexión Oracle/MySQL
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Evento e = new Evento();
                e.setID(rs.getInt("ID_EVENTO"));
                e.setNombre(rs.getString("NOMBRE"));
                e.setFecha_Inicio(rs.getTimestamp("FECHA_INICIO"));
                e.setFecha_Fin(rs.getTimestamp("FECHA_FIN"));
                lista.add(e);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar próximos eventos: " + ex.getMessage());
            ex.printStackTrace();
        }
        return lista;
    }

    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM USUARIOS WHERE ID_USUARIO = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Evento> obtenerMisEventos(int idUsuario) {
        List<Evento> lista = new ArrayList<>();

        // Consulta filtrada por la columna CREADO_POR
        String sql = "SELECT ID_EVENTO, NOMBRE, FECHA_INICIO, FECHA_FIN, TIPO_EVENTO, INSTITUCION " +
                "FROM EVENTOS WHERE CREADO_POR = ? ORDER BY FECHA_INICIO ASC";

        try (Connection con = DatabaseConnection.getConnection(); // Tu clase de conexión
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Asignamos el ID del usuario en sesión a la columna CREADO_POR
            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Evento e = new Evento();
                    e.setID(rs.getInt("ID_EVENTO"));
                    e.setNombre(rs.getString("NOMBRE"));
                    e.setFecha_Inicio(rs.getTimestamp("FECHA_INICIO"));
                    e.setFecha_Fin(rs.getTimestamp("FECHA_FIN"));
                    e.setTipo_Evento(rs.getString("TIPO_EVENTO"));
                    e.setInstitucion(rs.getString("INSTITUCION"));

                    lista.add(e);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar mis eventos: " + ex.getMessage());
            ex.printStackTrace();
        }
        return lista;
    }

    // MÉTODO PARA LISTAR DOCENTES CON CONTRASENA INCLUIDA
    public List<Usuario> GestionDocentes() {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT ID_USUARIO, NOMBRE, APELLIDO_PATERNO, APELLIDO_MATERNO, " +
                "CORREO_INSTITUCIONAL, ID_DIVISION, NUMERO_EMPLEADO, ACTIVO, CONTRASENA " +
                "FROM USUARIOS WHERE LOWER(ROL) = 'docente' ORDER BY NOMBRE ASC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario U = new Usuario();
                U.setIdUsuario(rs.getInt("ID_USUARIO"));
                U.setNombre(rs.getString("NOMBRE"));
                U.setApellidoPaterno(rs.getString("APELLIDO_PATERNO"));
                U.setApellidoMaterno(rs.getString("APELLIDO_MATERNO"));
                U.setCorreoInstitucional(rs.getString("CORREO_INSTITUCIONAL"));
                U.setIdDivision(rs.getInt("ID_DIVISION"));
                U.setNumeroEmpleado(rs.getString("NUMERO_EMPLEADO"));
                U.setActivo(rs.getInt("ACTIVO"));
                U.setContrasena(rs.getString("CONTRASENA"));

                lista.add(U);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar docentes: " + ex.getMessage());
            ex.printStackTrace();
        }
        return lista;
    }

    // ==========================================
    // MÉTODOS PARA CAMBIAR EL ESTADO (ACTIVO/INACTIVO)
    // ==========================================

    // Opción 1: Recibe ID y el nuevo estado (1 o 0)
    public boolean cambiarEstado(int idUsuario, int nuevoEstado) {
        String query = "UPDATE usuarios SET activo = ? WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, nuevoEstado);
            ps.setInt(2, idUsuario);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }



    public Usuario obtenerDocentePorId(int idUsuario) {
        Usuario u = null;
        // Consulta exacta a las columnas de tu DDL en Oracle
        String sql = "SELECT ID_USUARIO, NOMBRE, APELLIDO_PATERNO, APELLIDO_MATERNO, " +
                "CORREO_INSTITUCIONAL, ID_DIVISION, NUMERO_EMPLEADO, TELEFONO, ACTIVO, CONTRASENA " +
                "FROM USUARIOS WHERE ID_USUARIO = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setIdUsuario(rs.getInt("ID_USUARIO"));
                    u.setNombre(rs.getString("NOMBRE"));
                    u.setApellidoPaterno(rs.getString("APELLIDO_PATERNO"));
                    // Aseguramos que si viene null no rompa
                    u.setApellidoMaterno(rs.getString("APELLIDO_MATERNO") != null ? rs.getString("APELLIDO_MATERNO") : "");
                    u.setCorreoInstitucional(rs.getString("CORREO_INSTITUCIONAL"));
                    u.setIdDivision(rs.getInt("ID_DIVISION"));
                    u.setNumeroEmpleado(rs.getString("NUMERO_EMPLEADO"));
                    u.setTelefono(rs.getString("TELEFONO") != null ? rs.getString("TELEFONO") : "");
                    u.setActivo(rs.getInt("ACTIVO"));
                    u.setContrasena(rs.getString("CONTRASENA"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error SQL al obtener docente: " + ex.getMessage());
            ex.printStackTrace();
        }
        return u;
    }














    // Opción 2: Alterna el estado automáticamente (Si es 1 pasa a 0, si es 0 pasa a 1)
    public boolean cambiarEstado(int idUsuario) {
        String query = "UPDATE usuarios SET activo = CASE WHEN activo = 1 THEN 0 ELSE 1 END WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idUsuario);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al alternar estado de usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Alias por si en tus servlets lo buscas como cambiarEstadoUsuario
    public boolean cambiarEstadoUsuario(int idUsuario, int nuevoEstado) {
        return cambiarEstado(idUsuario, nuevoEstado);
    }

    public boolean cambiarEstadoUsuario(int idUsuario) {
        return cambiarEstado(idUsuario);
    }
    // ==========================================
    // MÉTODO PARA ACTUALIZAR PERFIL (TELÉFONO Y CONTRASEÑA)
    // ==========================================
    public boolean actualizarPerfil(int idUsuario, String telefono, String nuevaContrasena) {
        StringBuilder sql = new StringBuilder("UPDATE usuarios SET ");
        boolean tieneTelefono = (telefono != null && !telefono.trim().isEmpty());
        boolean tienePass = (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty());

        if (!tieneTelefono && !tienePass) {
            return false; // No hay nada que actualizar
        }

        if (tieneTelefono && tienePass) {
            sql.append("telefono = ?, contrasena = ? ");
        } else if (tieneTelefono) {
            sql.append("telefono = ? ");
        } else {
            sql.append("contrasena = ? ");
        }
        sql.append("WHERE id_usuario = ?");

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (tieneTelefono) {
                ps.setString(paramIndex++, telefono.trim());
            }
            if (tienePass) {
                ps.setString(paramIndex++, nuevaContrasena.trim());
            }
            ps.setInt(paramIndex, idUsuario);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar perfil: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}