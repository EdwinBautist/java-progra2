package papeleria.model.dao;

import papeleria.model.entity.Marca;
import papeleria.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAO {

    public List<Marca> obtenerTodas() throws SQLException {
        List<Marca> lista = new ArrayList<>();
        String sql = "SELECT * FROM Marca";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Marca(
                        rs.getInt("id_marca"),
                        rs.getString("nombre")
                ));
            }
        }
        return lista;
    }

    public boolean insertar(Marca marca) throws SQLException {
        String sql = "INSERT INTO Marca (nombre) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, marca.getNombre());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        marca.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean actualizar(Marca marca) throws SQLException {
        String sql = "UPDATE Marca SET nombre = ? WHERE id_marca = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, marca.getNombre());
            pstmt.setInt(2, marca.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idMarca) throws SQLException {
        String sql = "DELETE FROM Marca WHERE id_marca = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMarca);
            return pstmt.executeUpdate() > 0;
        }
    }
}