package papeleria.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Papeleria";
    private static final String DB_USER = "Daniel";
    private static final String DB_PASSWORD = "pape";
    private static final Properties DB_PROPERTIES = new Properties();

    private static Connection currentConnection = null;

    static {
        DB_PROPERTIES.setProperty("user", DB_USER);
        DB_PROPERTIES.setProperty("password", DB_PASSWORD);
        DB_PROPERTIES.setProperty("useSSL", "false");
        DB_PROPERTIES.setProperty("autoReconnect", "true");
        DB_PROPERTIES.setProperty("serverTimezone", "UTC");
        DB_PROPERTIES.setProperty("allowPublicKeyRetrieval", "true");
    }

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (currentConnection == null || currentConnection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                currentConnection = DriverManager.getConnection(DB_URL, DB_PROPERTIES);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver JDBC no encontrado", e);
            }
        }
        return currentConnection;
    }

    public static void closeConnection() {
        if (currentConnection != null) {
            try {
                if (!currentConnection.isClosed()) {
                    currentConnection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            } finally {
                currentConnection = null;
            }
        }
    }

    public static void rollback() {
        if (currentConnection != null) {
            try {
                if (!currentConnection.isClosed()) {
                    currentConnection.rollback();
                }
            } catch (SQLException e) {
                System.err.println("Error al hacer rollback: " + e.getMessage());
            }
        }
    }
}