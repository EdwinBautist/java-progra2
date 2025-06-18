package papeleria.login;

import papeleria.utils.DatabaseConnection;
import java.sql.*;

public class LoginManager {

    public static boolean autenticar(String email, String contrasena) {
        String sql = "SELECT 1 FROM Empleado WHERE email = ? AND contrasena = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, contrasena);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error de autenticación: " + e.getMessage());
            return false;
        }
    }
}