import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class TiendaApp extends JFrame {
    private JButton btnInsertar, btnMostrar;
    private Connection conexion;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DefaultTableModel modeloTabla;
    private JPanel panelInicial;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public TiendaApp() {
        setTitle("Sistema de Tienda");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Fecha Registro");

        panelInicial = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btnInsertar = new JButton("Insertar Producto");
        btnMostrar = new JButton("Mostrar Productos");
        panelInicial.add(btnInsertar);
        panelInicial.add(btnMostrar);

        crearVistaInsertar();
        crearVistaMostrar();

        cardPanel.add(panelInicial, "Inicio");

        btnInsertar.addActionListener(e -> cardLayout.show(cardPanel, "Insertar"));
        btnMostrar.addActionListener(e -> {
            actualizarTablaProductos();
            cardLayout.show(cardPanel, "Mostrar");
        });

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, "Inicio");
        conectarBD();
    }

    private void crearVistaInsertar() {
        JPanel panelInsertar = new JPanel(new GridLayout(5, 2, 10, 10));
        panelInsertar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtNombre = new JTextField();
        JTextField txtCantidad = new JTextField();
        JTextField txtPrecio = new JTextField();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnVolver = new JButton("Volver");

        panelInsertar.add(new JLabel("Nombre del Producto:"));
        panelInsertar.add(txtNombre);
        panelInsertar.add(new JLabel("Cantidad:"));
        panelInsertar.add(txtCantidad);
        panelInsertar.add(new JLabel("Precio Unitario:"));
        panelInsertar.add(txtPrecio);
        panelInsertar.add(btnVolver);
        panelInsertar.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            guardarProducto(txtNombre.getText(), txtCantidad.getText(), txtPrecio.getText());
            txtNombre.setText("");
            txtCantidad.setText("");
            txtPrecio.setText("");
            txtNombre.requestFocus();
        });

        btnVolver.addActionListener(e -> cardLayout.show(cardPanel, "Inicio"));

        cardPanel.add(panelInsertar, "Insertar");
    }

    private void crearVistaMostrar() {
        JPanel panelMostrar = new JPanel(new BorderLayout());
        JTable tablaProductos = new JTable(modeloTabla);
        tablaProductos.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        JButton btnVolver = new JButton("Volver");

        panelMostrar.add(scrollPane, BorderLayout.CENTER);
        panelMostrar.add(btnVolver, BorderLayout.SOUTH);

        btnVolver.addActionListener(e -> cardLayout.show(cardPanel, "Inicio"));

        cardPanel.add(panelMostrar, "Mostrar");
    }

    private void conectarBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/Tienda";
            String usuario = "edw";
            String contrasena = "Edw!nBDD2171";
            conexion = DriverManager.getConnection(url, usuario, contrasena);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void guardarProducto(String nombre, String cantidadStr, String precioStr) {
        try {
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre no puede estar vacío",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);

            String sql = "INSERT INTO producto (nombre, cantidad, precio) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nombre);
                pstmt.setInt(2, cantidad);
                pstmt.setDouble(3, precio);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Producto guardado",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser números",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en BD: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaProductos() {
        try {
            modeloTabla.setRowCount(0);

            String sql = "SELECT id, nombre, precio, cantidad, fecha_registro FROM producto ORDER BY nombre";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
                    String fechaFormateada = fechaRegistro != null ?
                            dateFormat.format(fechaRegistro) : "Sin fecha";

                    modeloTabla.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            String.format("$%.2f", rs.getDouble("precio")),
                            rs.getInt("cantidad"),
                            fechaFormateada
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al mostrar los productos: " + e.getMessage(),
                    "ERROR SQL",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TiendaApp().setVisible(true));
    }

    @Override
    protected void finalize() throws Throwable {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
        super.finalize();
    }
}