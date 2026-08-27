package mx.edu.utez.DesarrolloAcademico.utils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * CAMBIO IMPORTANTE:
 * Antes, cuando algo fallaba, este metodo devolvia null en silencio.
 * Entonces el DAO hacia con.prepareStatement(...) sobre un null y explotaba
 * con un NullPointerException que NO decia cual era el problema real.
 *
 * Ahora se lanza una SQLException con el motivo verdadero, asi el error
 * que ves en pantalla y en el log te dice que hay que arreglar.
 */
public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        try {
            Properties props = new Properties();
            try (InputStream in = DatabaseConnection.class.getClassLoader()
                    .getResourceAsStream("credentials.properties")) {
                if (in == null) {
                    throw new SQLException("No se encontro credentials.properties dentro del WAR (WEB-INF/classes).");
                }
                props.load(in);
            }

            String dbUser = props.getProperty("db.user");
            String dbPass = props.getProperty("db.pass");
            String dbUrl  = props.getProperty("db.url");

            if (dbUser == null || dbPass == null || dbUrl == null) {
                throw new SQLException("Faltan propiedades en credentials.properties (db.user, db.pass o db.url).");
            }

            // Ruta de la wallet dentro de resources. getPath() deja %20 en vez de
            // espacios si la ruta tiene espacios, por eso se decodifica.
            URL walletResource = DatabaseConnection.class.getClassLoader()
                    .getResource("Wallet_ProyectoIntegrador");
            if (walletResource == null) {
                throw new SQLException("No se encontro la carpeta Wallet_ProyectoIntegrador en los resources del WAR.");
            }
            String walletPath = URLDecoder.decode(walletResource.getPath(), StandardCharsets.UTF_8);
            System.setProperty("oracle.net.tns_admin", walletPath);

            Properties info = new Properties();
            info.put("user", dbUser);
            info.put("password", dbPass);

            Class.forName("oracle.jdbc.OracleDriver");

            return DriverManager.getConnection(dbUrl, info);

        } catch (SQLException e) {
            System.err.println("### FALLO DE CONEXION A ORACLE: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("### FALLO DE CONEXION A ORACLE: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("No se pudo conectar a la base de datos: " + e.getMessage(), e);
        }
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}