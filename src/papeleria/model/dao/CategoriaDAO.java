package papeleria.model.dao;

import papeleria.model.entity.Categoria;
import papeleria.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<Categoria> obtenerTodas() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM Categoria";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
                ));
            }
        }
        return lista;
    }

    public boolean insertar(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO Categoria (nombre) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, categoria.getNombre());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoria.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean actualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE Categoria SET nombre = ? WHERE id_categoria = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoria.getNombre());
            pstmt.setInt(2, categoria.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idCategoria) throws SQLException {
        String sql = "DELETE FROM Categoria WHERE id_categoria = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCategoria);
            return pstmt.executeUpdate() > 0;
        }
    }
}