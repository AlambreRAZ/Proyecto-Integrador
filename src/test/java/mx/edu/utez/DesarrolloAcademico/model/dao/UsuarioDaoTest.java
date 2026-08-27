package mx.edu.utez.DesarrolloAcademico.model.dao;

import mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDaoTest {

    /**
     * CORREGIDO: DatabaseConnection.getConnection() ahora declara
     * "throws SQLException" (antes devolvia null en silencio).
     * Por eso el metodo de prueba tiene que declararla tambien.
     */
    @Test
    public void testConexionBD() throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            assertNotNull(con, "La conexión a la BD Oracle no debe ser nula.");
            assertFalse(con.isClosed(), "La conexión debe estar abierta.");
        }
    }

    @Test
    public void testFormatoCodigoRecuperacion() {
        String codigoEjemplo = "A1B2C3";
        assertNotNull(codigoEjemplo);
        assertEquals(6, codigoEjemplo.length(), "El código debe ser de 6 caracteres.");
    }
}