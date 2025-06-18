package papeleria.model.dao;

import papeleria.model.entity.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import papeleria.utils.DatabaseConnection;

public class ProductoDAO {
    // ... otros métodos existentes ...

    public List<Producto> obtenerTodosProductosConStock() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = """
            SELECT p.id_producto, p.nombre, p.precio_venta, 
                   IFNULL(SUM(l.cantidad), 0) AS stock,
                   MAX(l.fecha_entrada) AS fecha_registro,
                   p.id_categoria, p.id_marca
            FROM Producto p 
            LEFT JOIN Lote l ON p.id_producto = l.id_producto 
            GROUP BY p.id_producto, p.nombre, p.precio_venta, p.id_categoria, p.id_marca
            ORDER BY p.nombre""";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecioVenta(rs.getDouble("precio_venta"));
                p.setStock(rs.getInt("stock"));
                p.setFechaRegistro(rs.getTimestamp("fecha_registro"));

                // Configurar categoría y marca (solo IDs)
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setIdMarca(rs.getInt("id_marca"));

                productos.add(p);
            }
        }
        return productos;
    }

    public void guardarProductoConLote(Producto producto, int cantidad, double precioCompra) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Guardar producto
            String sqlProducto = "INSERT INTO Producto (nombre, precio_venta, id_categoria, id_marca) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlProducto, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, producto.getNombre());
                stmt.setDouble(2, producto.getPrecioVenta());
                stmt.setInt(3, producto.getIdCategoria());
                stmt.setInt(4, producto.getIdMarca());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId(rs.getInt(1));
                    }
                }
            }

            // 2. Guardar lote
            String sqlLote = "INSERT INTO Lote (fecha_entrada, precio_uni, cantidad, id_producto) VALUES (CURRENT_DATE(), ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlLote)) {
                stmt.setDouble(1, precioCompra);
                stmt.setInt(2, cantidad);
                stmt.setInt(3, producto.getId());
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }
    public List<Producto> buscarProductos(String filtro) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = """
            SELECT p.id_producto, p.nombre, p.precio_venta, 
                   IFNULL(SUM(l.cantidad), 0) AS stock,
                   p.id_categoria, p.id_marca
            FROM Producto p 
            LEFT JOIN Lote l ON p.id_producto = l.id_producto 
            WHERE p.nombre LIKE ?
            GROUP BY p.id_producto, p.nombre, p.precio_venta, p.id_categoria, p.id_marca
            ORDER BY p.nombre""";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + filtro + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecioVenta(rs.getDouble("precio_venta"));
                    p.setStock(rs.getInt("stock"));
                    p.setIdCategoria(rs.getInt("id_categoria"));
                    p.setIdMarca(rs.getInt("id_marca"));
                    productos.add(p);
                }
            }
        }
        return productos;
    }
}
