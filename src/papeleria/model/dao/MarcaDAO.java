package papeleria.model.dao;

import papeleria.model.entity.Marca;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import papeleria.utils.DatabaseConnection;

public class MarcaDAO {
    public List<Marca> obtenerTodasMarcas() throws SQLException {
        List<Marca> marcas = new ArrayList<>();
        String sql = "SELECT * FROM Marca ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Marca m = new Marca();
                m.setId(rs.getInt("id_marca"));
                m.setNombre(rs.getString("nombre"));
                marcas.add(m);
            }
        }
        return marcas;
    }
}