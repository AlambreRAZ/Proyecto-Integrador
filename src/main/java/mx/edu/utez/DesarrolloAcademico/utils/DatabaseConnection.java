package mx.edu.utez.DesarrolloAcademico.utils;

import java.io.InputStream;
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

            // Debug en consola para verificar qué valores lee en tiempo de ejecución
            System.out.println("Cargando DB URL: " + dbUrl);
            System.out.println("Cargando DB User: " + dbUser);

            // 2. Validar que ninguna clave esté vacía
            if (dbUser == null || dbPass == null || dbUrl == null) {
                System.err.println("Faltan propiedades en credentials.properties (Revisa db.user, db.pass o db.url)");
                return null;
            }

            // 3. Forzar ruta local de la Wallet en Windows
            System.setProperty("oracle.net.tns_admin", "C:/Users/Lenovo/Downloads/Wallet_ProyectoIntegrador");

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