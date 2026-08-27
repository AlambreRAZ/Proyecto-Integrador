package mx.edu.utez.DesarrolloAcademico.model.dao;

import mx.edu.utez.DesarrolloAcademico.model.Periodo;
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
        // 1) Evitamos duplicados (si ya está asignado, lo damos por bueno)
        String verificar = "SELECT COUNT(*) FROM participantes_eventos WHERE id_evento = ? AND id_usuario = ?";
        String insertar  = "INSERT INTO participantes_eventos (id_evento, id_usuario, registrado_por, fecha_registro) " +
                "VALUES (?, ?, ?, SYSDATE)";

        try (Connection con = DatabaseConnection.getConnection()) {
            if (con == null) return false;
            con.setAutoCommit(true);   // <-- garantiza el COMMIT en Oracle

            try (PreparedStatement psV = con.prepareStatement(verificar)) {
                psV.setInt(1, idEvento);
                psV.setInt(2, idUsuario);
                try (ResultSet rs = psV.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return true; // ya estaba asignado
                    }
                }
            }

            try (PreparedStatement psI = con.prepareStatement(insertar)) {
                psI.setInt(1, idEvento);
                psI.setInt(2, idUsuario);
                psI.setInt(3, idRegistrador);
                return psI.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al asignar participante: " + e.getMessage());
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

    public boolean eliminarUsuario(int idUsuario) {
        String query = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean cambiarEstado(int idUsuario, int nuevoEstado) {
        String query = "UPDATE usuarios SET activo = ? WHERE id_usuario = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, nuevoEstado);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado del usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public int contarEventos() {
        int total = 0;
        String sql = "SELECT COUNT(ID_EVENTO) FROM EVENTOS";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar eventos: " + e.getMessage());
            e.printStackTrace();
        }

        return total;
    }

    public int contarDocentes(int idDivision) {
        String sql = "SELECT COUNT(*) FROM USUARIOS WHERE LOWER(ROL) = 'docente' AND ID_DIVISION = ?";
        int total = 0;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDivision);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al contar docentes: " + e.getMessage());
            e.printStackTrace();
        }

        return total;
    }

    public int contarDocentesD() {
        String sql = "SELECT COUNT(*) FROM USUARIOS WHERE LOWER(ROL) = 'docente'";
        int total = 0;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al contar docentes: " + e.getMessage());
            e.printStackTrace();
        }

        return total;
    }

    public List<Usuario> listarPorRolesYDivision(int idDivision, String... roles) {
        List<Usuario> lista = new ArrayList<>();
        if (roles == null || roles.length == 0) return lista;

        StringBuilder sb = new StringBuilder(
                "SELECT id_usuario, nombre, apellido_paterno, apellido_materno, " +
                        "correo_institucional, numero_empleado, id_division, telefono, activo, rol " +
                        "FROM usuarios " +
                        "WHERE id_division = ? AND LOWER(rol) IN ("
        );

        for (int i = 0; i < roles.length; i++) {
            sb.append(i == 0 ? "?" : ", ?");
        }
        sb.append(") ORDER BY nombre ASC");

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sb.toString())) {

            ps.setInt(1, idDivision);

            for (int i = 0; i < roles.length; i++) {
                ps.setString(i + 2, roles[i] != null ? roles[i].toLowerCase() : "");
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
            System.err.println("Error en listarPorRolesYDivision: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public boolean registrarPeriodo(Periodo periodo, int idUsuario) {
        String sql = "INSERT INTO periodos_carga (ID_DIVISION, FECHA_INICIO, FECHA_FIN, ACTIVO, CREADO_POR) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(true);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(periodo.getDivision()));
                ps.setDate(2, periodo.getFechaInicio());
                ps.setDate(3, periodo.getFechaFin());
                ps.setInt(4, periodo.isActivo() ? 1 : 0);
                ps.setInt(5, idUsuario);

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Periodo> obtenerTodosLosPeriodos() {
        List<Periodo> lista = new ArrayList<>();

        String sql = "SELECT p.ID_PERIODO, d.NOMBRE AS NOMBRE_DIVISION, p.FECHA_INICIO, p.FECHA_FIN, p.ACTIVO " +
                "FROM periodos_carga p " +
                "JOIN divisiones d ON p.ID_DIVISION = d.ID_DIVISION " +
                "ORDER BY p.ID_PERIODO DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Periodo p = new Periodo();
                p.setId(rs.getInt("ID_PERIODO"));
                p.setDivision(rs.getString("NOMBRE_DIVISION"));
                p.setFechaInicio(rs.getDate("FECHA_INICIO"));
                p.setFechaFin(rs.getDate("FECHA_FIN"));
                p.setActivo(rs.getInt("ACTIVO") == 1);

                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener periodos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public boolean eliminarPeriodo(int idPeriodo) {
        String sql = "DELETE FROM periodos_carga WHERE ID_PERIODO = ?";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(true);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idPeriodo);
                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar el periodo " + idPeriodo + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarEstadoPeriodo(int idPeriodo, boolean nuevoEstado) {
        String sql = "UPDATE periodos_carga SET ACTIVO = ? WHERE ID_PERIODO = ?";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(true);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, nuevoEstado ? 1 : 0);
                ps.setInt(2, idPeriodo);

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPeriodo(int idPeriodo, String division, String fechaInicio, String fechaFin) {
        String sql = "UPDATE periodos_carga SET " +
                "ID_DIVISION = (SELECT ID_DIVISION FROM divisiones WHERE (NOMBRE = ? OR TO_CHAR(ID_DIVISION) = ?) AND ROWNUM <= 1), " +
                "FECHA_INICIO = TO_DATE(?, 'YYYY-MM-DD'), " +
                "FECHA_FIN = TO_DATE(?, 'YYYY-MM-DD') " +
                "WHERE ID_PERIODO = ?";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(true);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, division);
                ps.setString(2, division);
                ps.setString(3, fechaInicio);
                ps.setString(4, fechaFin);
                ps.setInt(5, idPeriodo);

                int filasAfectadas = ps.executeUpdate();
                return filasAfectadas > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar periodo " + idPeriodo + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeDivision(String division, int idPeriodoExcluir) {
        String sql = "SELECT COUNT(*) FROM periodos_carga p " +
                "JOIN divisiones d ON p.ID_DIVISION = d.ID_DIVISION " +
                "WHERE (d.NOMBRE = ? OR TO_CHAR(d.ID_DIVISION) = ?) " +
                "AND (? = 0 OR p.ID_PERIODO != ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, division);
            ps.setString(2, division);
            ps.setInt(3, idPeriodoExcluir);
            ps.setInt(4, idPeriodoExcluir);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar duplicado de división: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public String obtenerNombreDivision(String divisionOrId) {
        String sql = "SELECT NOMBRE FROM divisiones WHERE NOMBRE = ? OR TO_CHAR(ID_DIVISION) = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, divisionOrId);
            ps.setString(2, divisionOrId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("NOMBRE");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombre de división: " + e.getMessage());
        }
        return divisionOrId;
    }

    public int obtenerOCrearPorCorreo(String correo, String nombre) {
        int idUsuario = 0;
        String sqlBuscar = "SELECT id_usuario FROM usuarios WHERE LOWER(correo_institucional) = LOWER(?)";
        String sqlInsertar = "INSERT INTO usuarios (nombre, correo_institucional, rol, activo) VALUES (?, ?, 'docente', 1)";

        try (Connection con = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
                ps.setString(1, correo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id_usuario");
                    }
                }
            }

            try (PreparedStatement psInsert = con.prepareStatement(sqlInsertar, new String[]{"ID_USUARIO"})) {
                psInsert.setString(1, nombre != null && !nombre.trim().isEmpty() ? nombre : "Docente");
                psInsert.setString(2, correo);

                int filas = psInsert.executeUpdate();
                if (filas > 0) {
                    try (ResultSet rsKeys = psInsert.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            idUsuario = rsKeys.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerOCrearPorCorreo: " + e.getMessage());
            e.printStackTrace();
        }
        return idUsuario;
    }

    public int obtenerIdPorCorreo(String correo) {
        int idUsuario = 0;
        String sql = "SELECT id_usuario FROM usuarios WHERE LOWER(correo_institucional) = LOWER(?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idUsuario = rs.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID por correo: " + e.getMessage());
            e.printStackTrace();
        }
        return idUsuario;
    }

    // MÉTODO AGREGADO: VERIFICA SI EL PERIODO DE LA DIVISIÓN ESTÁ ACTIVO Y VIGENTE
    public boolean tienePeriodoCargaActivo(int idDivision) {
        String sql = "SELECT COUNT(*) FROM periodos_carga " +
                "WHERE id_division = ? AND activo = 1 " +
                "AND CURRENT_DATE BETWEEN fecha_inicio AND fecha_fin";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDivision);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar periodo de carga: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}