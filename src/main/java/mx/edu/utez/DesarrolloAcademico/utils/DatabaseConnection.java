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
                }
            }

            String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : props.getProperty("db.user");
            String dbPass = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : props.getProperty("db.password");
            
            java.net.URL walletUrl = DatabaseConnection.class.getClassLoader().getResource("wallet");
            String walletPath = "";
            if (walletUrl != null) {
                walletPath = new java.io.File(walletUrl.toURI()).getAbsolutePath();
            }
            
            Properties info = new Properties();
            info.put("user", dbUser);
            info.put("password", dbPass);
            info.put("oracle.net.tns_admin", walletPath);

            Class.forName("oracle.jdbc.OracleDriver");

            String dbUrl = "jdbc:oracle:thin:@desarrolloacademico_medium";
            Connection newConnection = DriverManager.getConnection(dbUrl, info);
            System.out.println("Nueva conexión a Oracle exitosa!");
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
