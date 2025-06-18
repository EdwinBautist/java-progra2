package papeleria.model.dao;

import papeleria.model.entity.Venta;
import papeleria.model.entity.DetalleVenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import papeleria.utils.DatabaseConnection;

public class VentaDAO {
    private static final String SQL_INSERT_VENTA =
            "INSERT INTO Venta (fecha, precio_total, id_cliente, id_empleado) VALUES (?, ?, ?, ?)";
    private static final String SQL_INSERT_DETALLE =
            "INSERT INTO Producto_Venta (id_venta, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL =
            "SELECT id_venta, fecha, precio_total, id_cliente, id_empleado FROM Venta ORDER BY fecha DESC";
    private static final String SQL_SELECT_BY_ID =
            "SELECT id_venta, fecha, precio_total, id_cliente, id_empleado FROM Venta WHERE id_venta = ?";
    private static final String SQL_SELECT_DETALLES =
            "SELECT id_producto, cantidad, precio_unitario FROM Producto_Venta WHERE id_venta = ?";
    private static final String SQL_UPDATE_STOCK =
            "UPDATE Producto SET stock_actual = stock_actual - ? WHERE id_producto = ?";

    public int registrarVenta(Venta venta, List<DetalleVenta> detalles) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Registrar la venta principal
            int idVenta;
            try (PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT_VENTA, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setDate(1, new Date(venta.getFecha().getTime()));
                pstmt.setDouble(2, venta.getPrecioTotal());

                if (venta.getIdCliente() > 0) {
                    pstmt.setInt(3, venta.getIdCliente());
                } else {
                    pstmt.setNull(3, Types.INTEGER);
                }

                pstmt.setInt(4, venta.getIdEmpleado());
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idVenta = rs.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID de la venta generada");
                    }
                }
            }

            // 2. Registrar detalles y actualizar stock
            try (PreparedStatement pstmtDetalle = conn.prepareStatement(SQL_INSERT_DETALLE);
                 PreparedStatement pstmtStock = conn.prepareStatement(SQL_UPDATE_STOCK)) {

                for (DetalleVenta detalle : detalles) {
                    // Insertar detalle
                    pstmtDetalle.setInt(1, idVenta);
                    pstmtDetalle.setInt(2, detalle.getIdProducto());
                    pstmtDetalle.setInt(3, detalle.getCantidad());
                    pstmtDetalle.setDouble(4, detalle.getPrecioUnitario());
                    pstmtDetalle.addBatch();

                    // Actualizar stock
                    pstmtStock.setInt(1, detalle.getCantidad());
                    pstmtStock.setInt(2, detalle.getIdProducto());
                    pstmtStock.addBatch();
                }

                pstmtDetalle.executeBatch();
                pstmtStock.executeBatch();
            }

            conn.commit();
            return idVenta;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Venta> obtenerTodasVentas() throws SQLException {
        List<Venta> ventas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {

            while (rs.next()) {
                Venta venta = mapearVenta(rs);
                ventas.add(venta);
            }
        }
        return ventas;
    }

    public Venta obtenerVentaPorId(int idVenta) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            pstmt.setInt(1, idVenta);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Venta venta = mapearVenta(rs);
                    venta.setDetalles(obtenerDetallesVenta(conn, idVenta));
                    return venta;
                }
            }
        }
        return null;
    }

    private List<DetalleVenta> obtenerDetallesVenta(Connection conn, int idVenta) throws SQLException {
        List<DetalleVenta> detalles = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_DETALLES)) {
            pstmt.setInt(1, idVenta);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta detalle = new DetalleVenta(
                            idVenta,
                            rs.getInt("id_producto"),
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_unitario")
                    );
                    detalles.add(detalle);
                }
            }
        }
        return detalles;
    }

    private Venta mapearVenta(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setId(rs.getInt("id_venta"));
        venta.setFecha(rs.getDate("fecha"));
        venta.setPrecioTotal(rs.getDouble("precio_total"));

        int idCliente = rs.getInt("id_cliente");
        if (!rs.wasNull()) {
            venta.setIdCliente(idCliente);
        }

        venta.setIdEmpleado(rs.getInt("id_empleado"));
        return venta;
    }

    public List<Venta> buscarVentasPorFecha(Date fechaInicio, Date fechaFin) throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT id_venta, fecha, precio_total, id_cliente, id_empleado " +
                "FROM Venta WHERE fecha BETWEEN ? AND ? ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, new Date(fechaInicio.getTime()));
            pstmt.setDate(2, new Date(fechaFin.getTime()));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ventas.add(mapearVenta(rs));
                }
            }
        }
        return ventas;
    }
}
