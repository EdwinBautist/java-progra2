package papeleria.model.dao;

import papeleria.model.entity.Marca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAO {

    public List<Marca> obtenerTodasMarcas() throws SQLException {
        List<Marca> marcas = new ArrayList<>();
        String sql = "SELECT id_marca, nombre FROM Marca ORDER BY nombre";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                marcas.add(new Marca(rs.getInt("id_marca"), rs.getString("nombre")));
            }
        }
        return marcas;
    }

    public Marca obtenerMarcaPorId(int id) throws SQLException {
        String sql = "SELECT id_marca, nombre FROM Marca WHERE id_marca = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Marca(rs.getInt("id_marca"), rs.getString("nombre"));
                }
            }
        }
        return null; // Marca no encontrada
    }

    public int obtenerIdMarcaPorNombre(String nombre) throws SQLException {
        String sql = "SELECT id_marca FROM Marca WHERE nombre = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_marca");
                }
            }
        }
        return -1; // No encontrada
    }
}