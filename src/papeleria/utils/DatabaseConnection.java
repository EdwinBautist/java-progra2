package papeleria.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase utilitaria para manejar conexiones a la base de datos.
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/papeleria";
    private static final String DB_USER = "edwi";
    private static final String DB_PASSWORD = "Papeleria2024!";
    private static final Properties DB_PROPERTIES = new Properties();

    static {
        DB_PROPERTIES.setProperty("user", DB_USER);
        DB_PROPERTIES.setProperty("password", DB_PASSWORD);
        DB_PROPERTIES.setProperty("useSSL", "false");
        DB_PROPERTIES.setProperty("autoReconnect", "true");
        DB_PROPERTIES.setProperty("serverTimezone", "UTC");
        DB_PROPERTIES.setProperty("allowPublicKeyRetrieval", "true");
    }

    private DatabaseConnection() {} // Prevenir instanciación

    /**
     * Obtiene una conexión a la base de datos.
     * @return Objeto Connection
     * @throws SQLException Si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_PROPERTIES);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC no encontrado", e);
        }
    }

    /**
     * Cierra una conexión de manera segura.
     * @param conn La conexión a cerrar
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Realiza un rollback de manera segura.
     * @param conn La conexión donde hacer rollback
     */
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                System.err.println("Error al hacer rollback: " + e.getMessage());
            }
        }
    }
}