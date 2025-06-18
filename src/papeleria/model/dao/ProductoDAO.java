package papeleria.model.dao;

import papeleria.model.entity.Producto;
import papeleria.model.entity.Categoria;
import papeleria.model.entity.Marca;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import papeleria.utils.DatabaseConnection;

public class ProductoDAO {
    // Consultas SQL actualizadas para incluir stock_actual
    private static final String SQL_INSERT =
            "INSERT INTO Producto (nombre, precio_venta, id_categoria, id_marca, stock_actual) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL =
            "SELECT p.id_producto, p.nombre, p.precio_venta, p.stock_actual, " +
                    "c.id_categoria, c.nombre AS categoria_nombre, " +
                    "m.id_marca, m.nombre AS marca_nombre " +
                    "FROM Producto p " +
                    "JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                    "JOIN Marca m ON p.id_marca = m.id_marca";
    private static final String SQL_SELECT_BY_ID =
            "SELECT p.id_producto, p.nombre, p.precio_venta, p.stock_actual, " +
                    "c.id_categoria, c.nombre AS categoria_nombre, " +
                    "m.id_marca, m.nombre AS marca_nombre " +
                    "FROM Producto p " +
                    "JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                    "JOIN Marca m ON p.id_marca = m.id_marca " +
                    "WHERE p.id_producto = ?";
    private static final String SQL_UPDATE =
            "UPDATE Producto SET nombre = ?, precio_venta = ?, id_categoria = ?, id_marca = ?, stock_actual = ? WHERE id_producto = ?";
    private static final String SQL_DELETE =
            "DELETE FROM Producto WHERE id_producto = ?";
    private static final String SQL_SEARCH =
            "SELECT p.id_producto, p.nombre, p.precio_venta, p.stock_actual, " +
                    "c.id_categoria, c.nombre AS categoria_nombre, " +
                    "m.id_marca, m.nombre AS marca_nombre " +
                    "FROM Producto p " +
                    "JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                    "JOIN Marca m ON p.id_marca = m.id_marca " +
                    "WHERE p.nombre LIKE ? OR c.nombre LIKE ? OR m.nombre LIKE ?";
    private static final String SQL_UPDATE_STOCK =
            "UPDATE Producto SET stock_actual = stock_actual + ? WHERE id_producto = ?";
    private static final String SQL_SELECT_LOW_STOCK =
            "SELECT p.id_producto, p.nombre, p.stock_actual " +
                    "FROM Producto p WHERE p.stock_actual < ? ORDER BY p.stock_actual ASC";

    public void insertar(Producto producto) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecioVenta());
            pstmt.setInt(3, producto.getCategoria().getId());
            pstmt.setInt(4, producto.getMarca().getId());
            pstmt.setInt(5, producto.getStock());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    producto.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        }
        return productos;
    }

    public Producto obtenerPorId(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        }
        return null;
    }

    public void actualizar(Producto producto) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {

            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecioVenta());
            pstmt.setInt(3, producto.getCategoria().getId());
            pstmt.setInt(4, producto.getMarca().getId());
            pstmt.setInt(5, producto.getStock());
            pstmt.setInt(6, producto.getId());
            pstmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Producto> buscarProductos(String criterio) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String parametro = "%" + criterio + "%";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SEARCH)) {

            pstmt.setString(1, parametro);
            pstmt.setString(2, parametro);
            pstmt.setString(3, parametro);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }
        }
        return productos;
    }

    public void actualizarStock(int idProducto, int cantidad) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_STOCK)) {

            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, idProducto);
            pstmt.executeUpdate();
        }
    }

    public List<Producto> obtenerProductosBajoStock(int nivelMinimo) throws SQLException {
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_LOW_STOCK)) {

            pstmt.setInt(1, nivelMinimo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setStock(rs.getInt("stock_actual"));
                    productos.add(p);
                }
            }
        }
        return productos;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setPrecioVenta(rs.getDouble("precio_venta"));
        producto.setStock(rs.getInt("stock_actual"));

        Categoria categoria = new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("categoria_nombre")
        );

        Marca marca = new Marca(
                rs.getInt("id_marca"),
                rs.getString("marca_nombre")
        );

        producto.setCategoria(categoria);
        producto.setMarca(marca);

        return producto;
    }
}