package papeleria;

import papeleria.model.dao.*;
import papeleria.model.entity.*;
import papeleria.view.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import papeleria.utils.DatabaseConnection;

public class AppMain {
        private MainFrame mainFrame;
        private ProductoDAO productoDAO;
        private CategoriaDAO categoriaDAO;
        private MarcaDAO marcaDAO;
        private ClienteDAO clienteDAO;
        private DefaultTableModel modeloVenta;
        private int idEmpleadoSesion = 1; // ID del empleado logueado (debería venir del login)

        public AppMain() {
                try {
                        // Cargar driver JDBC
                        Class.forName("com.mysql.cj.jdbc.Driver");

                        // Inicializar componentes
                        mainFrame = new MainFrame();
                        productoDAO = new ProductoDAO();
                        categoriaDAO = new CategoriaDAO();
                        marcaDAO = new MarcaDAO();
                        clienteDAO = new ClienteDAO();

                        // Configurar modelo de tabla para ventas
                        modeloVenta = new DefaultTableModel(new Object[]{"ID", "Producto", "Precio", "Cantidad", "Subtotal"}, 0) {
                                @Override
                                public boolean isCellEditable(int row, int column) {
                                        return false;
                                }
                        };
                        mainFrame.getPanelVenta().getTablaVenta().setModel(modeloVenta);

                        setupEventListeners();
                } catch (ClassNotFoundException e) {
                        JOptionPane.showMessageDialog(null, "Error al cargar el driver JDBC: " + e.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                }
        }

        public void init() {
                mainFrame.setVisible(true);
                mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "Inicio");

                mainFrame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                                ConexionBD.closeConnection();
                        }
                });
        }

        private void setupEventListeners() {
                // Listeners existentes...
                mainFrame.getPanelInicio().getBtnRealizarVenta().addActionListener(e -> abrirPanelVenta());

                // Listeners para PanelVenta
                PanelVenta panelVenta = mainFrame.getPanelVenta();
                panelVenta.getBtnBuscar().addActionListener(e -> buscarProductosParaVenta());
                panelVenta.getTxtBusqueda().addActionListener(e -> buscarProductosParaVenta());
                panelVenta.getBtnAnadir().addActionListener(e -> anadirProductoAVenta());
                panelVenta.getBtnQuitar().addActionListener(e -> quitarProductoDeVenta());
                panelVenta.getBtnConfirmarVenta().addActionListener(e -> confirmarVenta());
                panelVenta.getBtnVolver().addActionListener(e -> volverAlInicio());
        }

        private void abrirPanelVenta() {
                try {
                        cargarClientesEnVenta();
                        mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "Venta");
                } catch (SQLException e) {
                        mostrarError("Error al cargar clientes: " + e.getMessage());
                }
        }

        private void cargarClientesEnVenta() throws SQLException {
                List<Cliente> clientes = clienteDAO.obtenerTodosClientes();
                mainFrame.getPanelVenta().cargarClientes(clientes);
        }

        private void buscarProductosParaVenta() {
                try {
                        String filtro = mainFrame.getPanelVenta().getTxtBusqueda().getText();
                        List<Producto> productos = productoDAO.buscarProductos(filtro);

                        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock"}, 0);
                        for (Producto p : productos) {
                                modelo.addRow(new Object[]{
                                        p.getId(),
                                        p.getNombre(),
                                        String.format("$%,.2f", p.getPrecioVenta()),
                                        p.getStock()
                                });
                        }
                        mainFrame.getPanelVenta().getTablaProductos().setModel(modelo);
                } catch (SQLException e) {
                        mostrarError("Error al buscar productos: " + e.getMessage());
                }
        }

        private void anadirProductoAVenta() {
                try {
                        int filaSeleccionada = mainFrame.getPanelVenta().getTablaProductos().getSelectedRow();
                        if (filaSeleccionada == -1) {
                                mostrarAdvertencia("Seleccione un producto primero");
                                return;
                        }

                        DefaultTableModel modeloProductos = (DefaultTableModel) mainFrame.getPanelVenta().getTablaProductos().getModel();
                        int idProducto = (int) modeloProductos.getValueAt(filaSeleccionada, 0);
                        String nombre = (String) modeloProductos.getValueAt(filaSeleccionada, 1);
                        double precio = Double.parseDouble(modeloProductos.getValueAt(filaSeleccionada, 2).toString().replace("$", "").replace(",", ""));
                        int stock = (int) modeloProductos.getValueAt(filaSeleccionada, 3);

                        int cantidad = (int) mainFrame.getPanelVenta().getSpinnerCantidad().getValue();

                        if (cantidad <= 0) {
                                mostrarAdvertencia("La cantidad debe ser mayor a cero");
                                return;
                        }

                        if (cantidad > stock) {
                                mostrarError("Stock insuficiente. Disponible: " + stock);
                                return;
                        }

                        // Verificar si el producto ya está en la venta
                        for (int i = 0; i < modeloVenta.getRowCount(); i++) {
                                if ((int) modeloVenta.getValueAt(i, 0) == idProducto) {
                                        int nuevaCantidad = (int) modeloVenta.getValueAt(i, 3) + cantidad;
                                        if (nuevaCantidad > stock) {
                                                mostrarError("La cantidad total excede el stock disponible");
                                                return;
                                        }
                                        modeloVenta.setValueAt(nuevaCantidad, i, 3);
                                        modeloVenta.setValueAt(precio * nuevaCantidad, i, 4);
                                        return;
                                }
                        }

                        // Añadir nuevo producto a la venta
                        modeloVenta.addRow(new Object[]{
                                idProducto,
                                nombre,
                                String.format("$%,.2f", precio),
                                cantidad,
                                String.format("$%,.2f", precio * cantidad)
                        });

                } catch (Exception e) {
                        mostrarError("Error al añadir producto: " + e.getMessage());
                }
        }

        private void quitarProductoDeVenta() {
                int filaSeleccionada = mainFrame.getPanelVenta().getTablaVenta().getSelectedRow();
                if (filaSeleccionada == -1) {
                        mostrarAdvertencia("Seleccione un producto de la venta");
                        return;
                }
                modeloVenta.removeRow(filaSeleccionada);
        }

        private void confirmarVenta() {
                if (modeloVenta.getRowCount() == 0) {
                        mostrarAdvertencia("No hay productos en la venta");
                        return;
                }

                int confirmacion = JOptionPane.showConfirmDialog(
                        mainFrame,
                        "¿Confirmar venta por un total de " + calcularTotalVentaFormateado() + "?",
                        "Confirmar Venta",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                        try {
                                registrarVentaEnBD();
                                modeloVenta.setRowCount(0);
                                JOptionPane.showMessageDialog(mainFrame, "Venta registrada exitosamente",
                                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException e) {
                                mostrarError("Error al registrar venta: " + e.getMessage());
                        }
                }
        }

        private void registrarVentaEnBD() throws SQLException {
                Connection conn = null;
                try {
                        conn = DatabaseConnection.getConnection();
                        conn.setAutoCommit(false);

                        // 1. Registrar la venta principal
                        Cliente cliente = (Cliente) mainFrame.getPanelVenta().getComboClientes().getSelectedItem();
                        int idCliente = cliente != null && cliente.getId() != 0 ? cliente.getId() : -1;
                        double total = calcularTotalVenta();

                        String sqlVenta = "INSERT INTO Venta (fecha, precio_total, id_cliente, id_empleado) VALUES (CURRENT_DATE(), ?, ?, ?)";
                        int idVenta;

                        try (PreparedStatement pstmt = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                                pstmt.setDouble(1, total);
                                if (idCliente == -1) {
                                        pstmt.setNull(2, Types.INTEGER);
                                } else {
                                        pstmt.setInt(2, idCliente);
                                }
                                pstmt.setInt(3, idEmpleadoSesion);
                                pstmt.executeUpdate();

                                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                                        if (rs.next()) {
                                                idVenta = rs.getInt(1);
                                        } else {
                                                throw new SQLException("No se pudo obtener el ID de la venta");
                                        }
                                }
                        }

                        // 2. Registrar detalles de venta y actualizar stock
                        String sqlDetalle = "INSERT INTO Producto_Venta (id_venta, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
                        String sqlActualizarStock = "UPDATE Producto SET stock_actual = stock_actual - ? WHERE id_producto = ?";

                        try (PreparedStatement pstmtDetalle = conn.prepareStatement(sqlDetalle);
                             PreparedStatement pstmtStock = conn.prepareStatement(sqlActualizarStock)) {

                                for (int i = 0; i < modeloVenta.getRowCount(); i++) {
                                        int idProducto = (int) modeloVenta.getValueAt(i, 0);
                                        int cantidad = (int) modeloVenta.getValueAt(i, 3);
                                        double precio = Double.parseDouble(modeloVenta.getValueAt(i, 2).toString().replace("$", "").replace(",", ""));

                                        // Insertar detalle
                                        pstmtDetalle.setInt(1, idVenta);
                                        pstmtDetalle.setInt(2, idProducto);
                                        pstmtDetalle.setInt(3, cantidad);
                                        pstmtDetalle.setDouble(4, precio);
                                        pstmtDetalle.addBatch();

                                        // Actualizar stock
                                        pstmtStock.setInt(1, cantidad);
                                        pstmtStock.setInt(2, idProducto);
                                        pstmtStock.addBatch();
                                }

                                pstmtDetalle.executeBatch();
                                pstmtStock.executeBatch();
                        }

                        conn.commit();
                } catch (SQLException e) {
                        if (conn != null) conn.rollback();
                        throw e;
                } finally {
                        if (conn != null) conn.setAutoCommit(true);
                }
        }

        private double calcularTotalVenta() {
                double total = 0;
                for (int i = 0; i < modeloVenta.getRowCount(); i++) {
                        String subtotalStr = modeloVenta.getValueAt(i, 4).toString().replace("$", "").replace(",", "");
                        total += Double.parseDouble(subtotalStr);
                }
                return total;
        }

        private String calcularTotalVentaFormateado() {
                return String.format("$%,.2f", calcularTotalVenta());
        }

        private void volverAlInicio() {
                modeloVenta.setRowCount(0);
                mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "Inicio");
        }

        private void mostrarError(String mensaje) {
                JOptionPane.showMessageDialog(mainFrame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }

        private void mostrarAdvertencia(String mensaje) {
                JOptionPane.showMessageDialog(mainFrame, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        public static void main(String[] args) {
                SwingUtilities.invokeLater(() -> {
                        AppMain app = new AppMain();
                        app.init();
                });
        }
}