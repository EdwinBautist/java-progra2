package papeleria.model.dao;

import papeleria.model.entity.Producto;
import papeleria.model.entity.Categoria;
import papeleria.model.entity.Marca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    /**
     * Inserta un nuevo producto en la base de datos y su lote inicial.
     * Asume que el Producto ya tiene objetos Categoria y Marca válidos (con ID).
     *
     * @param producto El objeto Producto a insertar.
     * @param cantidadInicial La cantidad inicial de stock para este producto.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public void insertarProducto(Producto producto, int cantidadInicial) throws SQLException {
        String sqlProducto = "INSERT INTO Producto (nombre, precio_venta, id_categoria, id_marca, stock_actual) VALUES (?, ?, ?, ?, ?)";
        String sqlLote = "INSERT INTO Lote (id_producto, cantidad, fecha_entrada, id_proveedor, id_compra) VALUES (?, ?, CURDATE(), NULL, NULL)"; // Asumiendo que id_proveedor e id_compra pueden ser NULL para lote inicial

        Connection conn = null;
        PreparedStatement stmtProducto = null;
        PreparedStatement stmtLote = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar el Producto
            stmtProducto = conn.prepareStatement(sqlProducto, Statement.RETURN_GENERATED_KEYS);
            stmtProducto.setString(1, producto.getNombre());
            stmtProducto.setDouble(2, producto.getPrecioVenta());
            stmtProducto.setInt(3, producto.getCategoria().getId()); // ID de la categoría
            stmtProducto.setInt(4, producto.getMarca().getId());    // ID de la marca
            stmtProducto.setInt(5, cantidadInicial); // Stock inicial
            stmtProducto.executeUpdate();

            // Obtener el ID generado para el producto
            rs = stmtProducto.getGeneratedKeys();
            if (rs.next()) {
                producto.setId(rs.getInt(1)); // Asignar el ID al objeto Producto
            } else {
                throw new SQLException("No se pudo obtener el ID del producto insertado.");
            }

            // 2. Insertar el Lote inicial para ese producto
            stmtLote = conn.prepareStatement(sqlLote);
            stmtLote.setInt(1, producto.getId());
            stmtLote.setInt(2, cantidadInicial);
            stmtLote.executeUpdate();

            conn.commit(); // Confirmar la transacción
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir la transacción en caso de error
                } catch (SQLException ex) {
                    System.err.println("Error al realizar rollback: " + ex.getMessage());
                }
            }
            throw e; // Relanzar la excepción para que sea manejada por AppMain
        } finally {
            // Cerrar recursos en orden inverso de apertura
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmtProducto != null) try { stmtProducto.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmtLote != null) try { stmtLote.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaurar auto-commit
                    // La conexión se cierra en ConexionBD.closeConnection() cuando la aplicación termina.
                    // Si tienes un pool de conexiones, aquí devolverías la conexión al pool.
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Busca productos en la base de datos por nombre.
     * @param filtro El texto para filtrar por nombre de producto. Si es nulo o vacío, trae todos.
     * @return Una lista de productos que coinciden con el filtro.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public List<Producto> buscarProductos(String filtro) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre AS producto_nombre, p.precio_venta, p.stock_actual, " +
                "c.id_categoria, c.nombre AS categoria_nombre, " +
                "m.id_marca, m.nombre AS marca_nombre " +
                "FROM Producto p " +
                "JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                "JOIN Marca m ON p.id_marca = m.id_marca " +
                "WHERE p.nombre LIKE ? OR c.nombre LIKE ? OR m.nombre LIKE ?"; // Buscar en nombre de producto, categoría o marca

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String likeFiltro = "%" + (filtro != null ? filtro : "") + "%";
            stmt.setString(1, likeFiltro);
            stmt.setString(2, likeFiltro);
            stmt.setString(3, likeFiltro);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Categoria categoria = new Categoria(rs.getInt("id_categoria"), rs.getString("categoria_nombre"));
                    Marca marca = new Marca(rs.getInt("id_marca"), rs.getString("marca_nombre"));
                    Producto producto = new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("producto_nombre"),
                            rs.getDouble("precio_venta"),
                            categoria,
                            marca,
                            rs.getInt("stock_actual") // Cargar el stock_actual
                    );
                    productos.add(producto);
                }
            }
        }
        return productos;
    }

    /**
     * Obtiene un producto específico por su ID.
     * @param idProducto El ID del producto a buscar.
     * @return El objeto Producto si se encuentra, null en caso contrario.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public Producto obtenerProductoPorId(int idProducto) throws SQLException {
        String sql = "SELECT p.id_producto, p.nombre AS producto_nombre, p.precio_venta, p.stock_actual, " +
                "c.id_categoria, c.nombre AS categoria_nombre, " +
                "m.id_marca, m.nombre AS marca_nombre " +
                "FROM Producto p " +
                "JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                "JOIN Marca m ON p.id_marca = m.id_marca " +
                "WHERE p.id_producto = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Categoria categoria = new Categoria(rs.getInt("id_categoria"), rs.getString("categoria_nombre"));
                    Marca marca = new Marca(rs.getInt("id_marca"), rs.getString("marca_nombre"));
                    return new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("producto_nombre"),
                            rs.getDouble("precio_venta"),
                            categoria,
                            marca,
                            rs.getInt("stock_actual")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param producto El objeto Producto con los datos actualizados.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE Producto SET nombre = ?, precio_venta = ?, id_categoria = ?, id_marca = ?, stock_actual = ? WHERE id_producto = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecioVenta());
            stmt.setInt(3, producto.getCategoria().getId());
            stmt.setInt(4, producto.getMarca().getId());
            stmt.setInt(5, producto.getStock()); // Asume que el stock_actual se puede actualizar directamente
            stmt.setInt(6, producto.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina un producto de la base de datos por su ID.
     * Considera que esto puede necesitar la eliminación en cascada de lotes o manejo de claves foráneas.
     * Tu SQL dump indica ON DELETE CASCADE para Producto_Venta y Producto_Compra.
     * Si Lote no tiene CASCADE para id_producto, esta operación fallará si hay lotes asociados.
     *
     * @param idProducto El ID del producto a eliminar.
     * @throws SQLException Si ocurre un error de acceso a la base de datos (ej. violación de FK).
     */
    public void eliminarProducto(int idProducto) throws SQLException {
        // Primero, si no tienes ON DELETE CASCADE configurado para Lote en tu DB,
        // deberías eliminar los lotes asociados antes de eliminar el producto.
        // String deleteLotesSql = "DELETE FROM Lote WHERE id_producto = ?";
        String deleteProductoSql = "DELETE FROM Producto WHERE id_producto = ?";

        Connection conn = null;
        PreparedStatement stmtProducto = null;
        // PreparedStatement stmtLotes = null; // Si necesitas eliminar lotes manualmente

        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // Si necesitas eliminar lotes manualmente:
            // stmtLotes = conn.prepareStatement(deleteLotesSql);
            // stmtLotes.setInt(1, idProducto);
            // stmtLotes.executeUpdate();

            stmtProducto = conn.prepareStatement(deleteProductoSql);
            stmtProducto.setInt(1, idProducto);
            int filasAfectadas = stmtProducto.executeUpdate();

            if (filasAfectadas == 0) {
                conn.rollback(); // No se eliminó nada, hacer rollback
                throw new SQLException("No se encontró el producto con ID: " + idProducto + " para eliminar.");
            }

            conn.commit(); // Confirmar la transacción
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al realizar rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            // if (stmtLotes != null) try { stmtLotes.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmtProducto != null) try { stmtProducto.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}