package papeleria.model.dao;

import papeleria.model.entity.Producto;
import papeleria.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    public List<Producto> obtenerTodosProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, precio_venta, stock_actual, id_categoria, id_marca FROM Producto";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecioVenta(rs.getDouble("precio_venta"));
                producto.setStock(rs.getInt("stock_actual"));
                producto.setIdCategoria(rs.getInt("id_categoria"));
                producto.setIdMarca(rs.getInt("id_marca"));
                productos.add(producto);
            }
        }
        return productos;
    }

    public Producto obtenerProductoPorId(int id) throws SQLException {
        String sql = "SELECT id_producto, nombre, precio_venta, stock_actual, id_categoria, id_marca FROM Producto WHERE id_producto = ?";
        Producto producto = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto();
                    producto.setId(rs.getInt("id_producto"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setPrecioVenta(rs.getDouble("precio_venta"));
                    producto.setStock(rs.getInt("stock_actual"));
                    producto.setIdCategoria(rs.getInt("id_categoria"));
                    producto.setIdMarca(rs.getInt("id_marca"));
                }
            }
        }
        return producto;
    }

    public void eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM Producto WHERE id_producto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Producto> buscarProductos(String filtro) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, precio_venta, stock_actual FROM Producto WHERE nombre LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + filtro + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto();
                    producto.setId(rs.getInt("id_producto"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setPrecioVenta(rs.getDouble("precio_venta"));
                    producto.setStock(rs.getInt("stock_actual"));
                    productos.add(producto);
                }
            }
        }
        return productos;
    }

    public boolean actualizarStock(int idProducto, int cantidad) throws SQLException {
        String sql = "UPDATE Producto SET stock_actual = stock_actual - ? WHERE id_producto = ? AND stock_actual >= ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, idProducto);
            pstmt.setInt(3, cantidad);

            return pstmt.executeUpdate() > 0;
        }
    }
}