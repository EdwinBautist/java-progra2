package papeleria.login;



import papeleria.model.dao.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginManager {

    public enum RolUsuario {
        VENDEDOR,
        GERENTE,
        INVALIDO
    }

    public static RolUsuario autenticar(String email, String contrasena) {
        String sql = "SELECT puesto FROM Empleado WHERE email = ? AND contrasena = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, contrasena);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String puesto = rs.getString("puesto");
                    if (puesto.equalsIgnoreCase("vendedor")) {
                        return RolUsuario.VENDEDOR;
                    } else if (puesto.equalsIgnoreCase("gerente")) {
                        return RolUsuario.GERENTE;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Esto podría mejorarse para mostrar errores en UI si se desea
        }

        return RolUsuario.INVALIDO;
    }
}