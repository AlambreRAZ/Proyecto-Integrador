package mx.edu.utez.DesarrolloAcademico.utils;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    public static Connection getConnection() {
        try {
            Properties props = new Properties();
            try (InputStream in = DatabaseConnection.class.getClassLoader().getResourceAsStream("credentials.properties")) {
                if (in != null) {
                    props.load(in);
                } else {
                    System.err.println("No se encontró credentials.properties");
                    return null;
                }
            }

            // 1. Obtener propiedades del archivo .properties
            String dbUser = props.getProperty("db.user");
            String dbPass = props.getProperty("db.pass");
            String dbUrl = props.getProperty("db.url");

            System.out.println("Cargando DB URL: " + dbUrl);
            System.out.println("Cargando DB User: " + dbUser);

            // 2. Validar que ninguna clave esté vacía
            if (dbUser == null || dbPass == null || dbUrl == null) {
                System.err.println("Faltan propiedades en credentials.properties (Revisa db.user, db.pass o db.url)");
                return null;
            }

            // 3. RUTA DINÁMICA DE LA WALLET (Funciona en Windows y en Linux)
            URL walletResource = DatabaseConnection.class.getClassLoader().getResource("Wallet_ProyectoIntegrador");
            if (walletResource != null) {
                // Asigna la ruta de la Wallet que está dentro de resources
                System.setProperty("oracle.net.tns_admin", walletResource.getPath());
                System.out.println("Wallet cargada desde: " + walletResource.getPath());
            } else {
                System.err.println("No se encontró la carpeta Wallet_ProyectoIntegrador en los resources");
            }

            Properties info = new Properties();
            info.put("user", dbUser);
            info.put("password", dbPass);

            Class.forName("oracle.jdbc.OracleDriver");

            Connection newConnection = DriverManager.getConnection(dbUrl, info);
            System.out.println("Conexión exitosa a la base de datos.");
            return newConnection;

        } catch (Exception e) {
            System.err.println("Error en la conexión a Base de Datos: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}