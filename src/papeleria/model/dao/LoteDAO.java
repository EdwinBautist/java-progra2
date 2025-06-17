package papeleria.model.dao;

import papeleria.model.entity.Lote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoteDAO {

    public void insertarLote(Lote lote) throws SQLException {
        // CURDATE() obtiene la fecha actual del servidor MySQL
        // Los campos 'fecha_salida', 'id_proveedor', 'id_compra' son NULL por defecto o se gestionan en otras operaciones.
        // Aquí solo insertamos los campos relevantes para el stock inicial del producto.
        String sql = "INSERT INTO Lote (fecha_entrada, precio_uni, cantidad, id_producto) VALUES (CURDATE(), ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, lote.getPrecioUni());
            pstmt.setInt(2, lote.getCantidad());
            pstmt.setInt(3, lote.getIdProducto());
            pstmt.executeUpdate();
        }
    }

    public void eliminarLotesPorProducto(int idProducto) throws SQLException {
        String sql = "DELETE FROM Lote WHERE id_producto = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            pstmt.executeUpdate();
        }
    }
}