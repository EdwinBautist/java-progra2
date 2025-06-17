package main.java.services;
import models.Usuario;
import utils.DatabaseConnection;
import java.sql.*;

public class AuthService {
    public Usuario validarCredenciales(String email, String contrasena) throws SQLException {
        String query = "SELECT id_empleado, email, contrasena, nombre, cargo FROM Empleado WHERE email = ? AND contrasena = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, contrasena); // ¡En producción, usa BCrypt para comparar hashes!

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_empleado"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getString("nombre"),
                        rs.getString("cargo")
                );
            }
        }
        return null; // Credenciales inválidas
    }
}