package papeleria.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static Connection connection;
    // --- Configura estos valores con los de tu base de datos ---
    private static final String URL = "jdbc:mysql://localhost:3306/Papeleria"; // ¡IMPORTANTE! Nombre de tu BD con P mayúscula
    private static final String USUARIO = "Daniel"; // Tu usuario de MySQL
    private static final String CONTRASENA = "pape"; // Tu contraseña de MySQL

    /**
     * Obtiene una instancia de la conexión a la base de datos.
     * Si la conexión no existe o está cerrada, intenta establecer una nueva.
     * @return Objeto Connection activo.
     * @throws SQLException Si ocurre un error al conectar.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // La siguiente línea es opcional en JDBC 4.0+, pero no hace daño
                // Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                System.out.println("Conexión a la base de datos establecida.");
            } catch (SQLException e) { // Quitamos ClassNotFoundException, si el driver no está, fallará en DriverManager.getConnection
                throw new SQLException("Error al conectar a la base de datos. Asegúrate que MySQL está corriendo y el driver JDBC está en el classpath. " + e.getMessage());
            }
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}