package papeleria.model.dao;

import papeleria.model.entity.Producto;
import papeleria.model.entity.Categoria;
import papeleria.model.entity.Marca;

import java.sql.Connection;
import java.sql.Date; // Asegúrate de usar java.sql.Date para la fecha del lote
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // DAOs para obtener objetos completos de Categoria y Marca
    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private MarcaDAO marcaDAO = new MarcaDAO();
    private LoteDAO loteDAO = new LoteDAO(); // Para manejar operaciones de lote

    /**
     * Inserta un nuevo producto y su lote inicial en la base de datos.
     * La operación es transaccional: si falla el producto o el lote, se deshace todo.
     * @param producto El objeto Producto a insertar (sin ID, con objetos Categoria y Marca completos).
     * @param cantidadLote La cantidad inicial para el lote asociado al producto.
     * @return El ID generado para el nuevo producto.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public int insertarProducto(Producto producto, int cantidadLote) throws SQLException {
        String sqlProducto = "INSERT INTO Producto (nombre, precio_venta, id_categoria, id_marca) VALUES (?, ?, ?, ?)";
        int idGenerado = -1;
        Connection conn = null;

        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); // Iniciar una transacción

            // Insertar el producto
            try (PreparedStatement pstmt = conn.prepareStatement(sqlProducto, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, producto.getNombre());
                pstmt.setDouble(2, producto.getPrecioVenta());
                pstmt.setInt(3, producto.getCategoria().getIdCategoria()); // Usar el ID del objeto Categoria
                pstmt.setInt(4, producto.getMarca().getIdMarca());       // Usar el ID del objeto Marca
                pstmt.executeUpdate();

                // Obtener el ID generado
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                        producto.setIdProducto(idGenerado); // Asignar el ID al objeto Producto
                    }
                }
            }

            // Si el producto se insertó, insertar el lote inicial
            if (idGenerado != -1) {
                // Usamos java.sql.Date y el precio de venta del producto como precio unitario del lote inicial
                loteDAO.insertarLote(new papeleria.model.entity.Lote(new Date(System.currentTimeMillis()), producto.getPrecioVenta(), cantidadLote, idGenerado));
            }

            conn.commit(); // Confirmar todas las operaciones si todo fue bien
            return idGenerado;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Deshacer si algo falló
            }
            throw e; // Relanzar la excepción para que sea manejada por la capa superior
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true); // Restaurar el modo auto-commit
            }
        }
    }

    /**
     * Actualiza la información de un producto existente.
     * @param producto El objeto Producto con los datos actualizados (debe tener el ID).
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE Producto SET nombre = ?, precio_venta = ?, id_categoria = ?, id_marca = ? WHERE id_producto = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecioVenta());
            pstmt.setInt(3, producto.getCategoria().getIdCategoria());
            pstmt.setInt(4, producto.getMarca().getIdMarca());
            pstmt.setInt(5, producto.getIdProducto());
            pstmt.executeUpdate();
        }
    }

    /**
     * Elimina un producto y todos sus lotes asociados de la base de datos.
     * La operación es transaccional.
     * @param idProducto El ID del producto a eliminar.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void eliminarProducto(int idProducto) throws SQLException {
        Connection conn = null;
        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // Primero eliminar los lotes asociados para evitar errores de clave foránea
            loteDAO.eliminarLotesPorProducto(idProducto);

            // Luego eliminar el producto
            String sqlProducto = "DELETE FROM Producto WHERE id_producto = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlProducto)) {
                pstmt.setInt(1, idProducto);
                pstmt.executeUpdate();
            }
            conn.commit(); // Confirmar la transacción

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Deshacer si algo falló
            }
            throw e; // Relanzar la excepción
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true); // Restaurar auto-commit
            }
        }
    }

    /**
     * Busca productos por un filtro de nombre y retorna una lista de Productos con su stock total.
     * @param filtroNombre El nombre o parte del nombre a buscar.
     * @return Lista de objetos Producto.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public List<Producto> buscarProductos(String filtroNombre) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = """
            SELECT p.id_producto, p.nombre, p.precio_venta, IFNULL(SUM(l.cantidad), 0) AS total_cantidad, MAX(l.fecha_entrada) AS ultima_fecha
            FROM Producto p
            LEFT JOIN Lote l ON p.id_producto = l.id_producto
            WHERE p.nombre LIKE ?
            GROUP BY p.id_producto, p.nombre, p.precio_venta
            ORDER BY p.nombre
        """;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + filtroNombre + "%"); // El '%' permite buscar coincidencias parciales
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Crea un objeto Producto con los datos para la tabla de listado
                    productos.add(new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getDouble("precio_venta"),
                            rs.getInt("total_cantidad"),
                            rs.getTimestamp("ultima_fecha")
                    ));
                }
            }
        }
        return productos;
    }

    /**
     * Obtiene un producto por su ID, incluyendo sus objetos Categoria y Marca completos.
     * @param idProducto El ID del producto a buscar.
     * @return El objeto Producto encontrado, o null si no existe.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public Producto obtenerProductoPorId(int idProducto) throws SQLException {
        String sql = "SELECT p.id_producto, p.nombre, p.precio_venta, p.id_categoria, p.id_marca " +
                "FROM Producto p WHERE p.id_producto = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Carga los objetos Categoria y Marca completos usando sus DAOs
                    Categoria categoria = categoriaDAO.obtenerCategoriaPorId(rs.getInt("id_categoria"));
                    Marca marca = marcaDAO.obtenerMarcaPorId(rs.getInt("id_marca"));
                    return new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getDouble("precio_venta"),
                            categoria,
                            marca
                    );
                }
            }
        }
        return null;
    }
}