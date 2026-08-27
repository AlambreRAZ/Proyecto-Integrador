package mx.edu.utez.DesarrolloAcademico.model.dao;

import mx.edu.utez.DesarrolloAcademico.utils.DatabaseConnection;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDaoTest {

    @Test
    public void testConexionBD() {
        Connection con = DatabaseConnection.getConnection();
        assertNotNull(con, "La conexión a la BD Oracle no debe ser nula.");
    }

    @Test
    public void testFormatoCodigoRecuperacion() {
        String codigoEjemplo = "A1B2C3";
        assertNotNull(codigoEjemplo);
        assertEquals(6, codigoEjemplo.length(), "El código debe ser de 6 caracteres.");
    }
}