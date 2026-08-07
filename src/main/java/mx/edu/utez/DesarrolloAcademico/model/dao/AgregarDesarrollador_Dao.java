package mx.edu.utez.DesarrolloAcademico.model.dao;

import mx.edu.utez.DesarrolloAcademico.model.Usuario;
import mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class AgregarDesarrollador_Dao {


    public boolean registrarDesarrollador(Usuario usuario) {
        boolean estado = false;
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DatabaseConnection.getConnection();

            String query = "INSERT INTO usuarios " +
                    "(nombre, apellido_paterno, apellido_materno, rol, id_division, numero_empleado, telefono, correo_institucional, contrasena, fecha_registro, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, 1)";

            ps = con.prepareStatement(query);
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

            int filas = ps.executeUpdate();
            estado = filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
            try { if (con != null) con.close(); } catch (SQLException ignored) {}
        }

        return estado;
    }

    // Revisa si ya existe un usuario con ese correo o número de empleado,
    // para no dejar que se registren duplicados (la tabla probablemente
    // tenga una restricción UNIQUE, pero así damos un mensaje claro antes de llegar ahí).
    public boolean existeCorreoOEmpleado(String correo, String numeroEmpleado) {
        boolean existe = false;
        String query = "SELECT COUNT(*) FROM usuarios WHERE correo_institucional = ? OR numero_empleado = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, correo);
            ps.setString(2, numeroEmpleado);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    existe = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return existe;
    }

    // Igual que existeCorreoOEmpleado, pero ignora al propio usuario que se está editando
    // (si no, se marcaría como "duplicado" contra sí mismo al guardar sin cambiar esos datos).
    public boolean existeCorreoOEmpleadoExcluyendo(String correo, String numeroEmpleado, int idUsuario) {
        boolean existe = false;
        String query = "SELECT COUNT(*) FROM usuarios WHERE (correo_institucional = ? OR numero_empleado = ?) AND id_usuario <> ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, correo);
            ps.setString(2, numeroEmpleado);
            ps.setInt(3, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    existe = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return existe;
    }


    public Usuario obtenerPorId(int idUsuario) {
        Usuario usuario = null;
        String query = "SELECT * FROM usuarios WHERE id_usuario = ? AND rol = 'desarrollo'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
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
                    usuario.setActivo(rs.getInt("activo"));

                    usuario.setContrasena(rs.getString("contrasena"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    // Actualiza los datos de un desarrollador existente.
    // Si nuevaContrasena viene null o vacía, la contraseña actual NO se toca.
    public boolean actualizarDesarrollador(Usuario usuario, String nuevaContrasena) {
        boolean estado = false;
        Connection con = null;
        PreparedStatement ps = null;

        boolean cambiaPassword = nuevaContrasena != null && !nuevaContrasena.isEmpty();

        try {
            con = DatabaseConnection.getConnection();

            String query = "UPDATE usuarios SET " +
                    "nombre = ?, apellido_paterno = ?, apellido_materno = ?, id_division = ?, " +
                    "numero_empleado = ?, telefono = ?, correo_institucional = ?" +
                    (cambiaPassword ? ", contrasena = ?" : "") +
                    " WHERE id_usuario = ?";

            ps = con.prepareStatement(query);
            int i = 1;
            ps.setString(i++, usuario.getNombre());
            ps.setString(i++, usuario.getApellidoPaterno());
            ps.setString(i++, usuario.getApellidoMaterno());

            if (usuario.getIdDivision() != null) {
                ps.setInt(i++, usuario.getIdDivision());
            } else {
                ps.setNull(i++, java.sql.Types.INTEGER);
            }

            ps.setString(i++, usuario.getNumeroEmpleado());
            ps.setString(i++, usuario.getTelefono());
            ps.setString(i++, usuario.getCorreoInstitucional());

            if (cambiaPassword) {
                ps.setString(i++, nuevaContrasena); // TODO: hashear con BCrypt antes de guardar
            }

            ps.setInt(i, usuario.getIdUsuario());

            int filas = ps.executeUpdate();
            estado = filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
            try { if (con != null) con.close(); } catch (SQLException ignored) {}
        }

        return estado;
    }

    // Trae a todos los usuarios con rol 'desarrollo', para la tabla de "Gestión de Desarrolladores".
    public List<Usuario> listarDesarrolladores() {
        List<Usuario> lista = new ArrayList<>();
        String query = "SELECT * FROM usuarios WHERE rol = 'desarrollo' ORDER BY nombre, apellido_paterno";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
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
                usuario.setActivo(rs.getInt("activo"));
                // No se mapea la contraseña: nunca se debe regresar al cliente.

                lista.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Activa o desactiva a un desarrollador (columna "activo"), sin borrar su registro.
    // Un usuario inactivo no puede iniciar sesión
    public boolean cambiarEstado(int idUsuario, int nuevoEstado) {
        String query = "UPDATE usuarios SET activo = ? WHERE id_usuario = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, nuevoEstado);
            ps.setInt(2, idUsuario);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}