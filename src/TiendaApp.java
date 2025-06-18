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
        setTitle("Papelería la nota");
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

        conectarBD();

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

    }

    private void crearVistaInsertar() {
        JPanel panelInsertar = new JPanel(new GridLayout(7, 2, 10, 10));
        panelInsertar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtNombre = new JTextField();
        JTextField txtCantidad = new JTextField();
        JTextField txtPrecio = new JTextField();

        JComboBox<String> comboCategoria = new JComboBox<>();
        JComboBox<String> comboMarca = new JComboBox<>();
        cargarCategorias(comboCategoria);
        cargarMarcas(comboMarca);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnVolver = new JButton("Volver");

        panelInsertar.add(new JLabel("Nombre del Producto:"));
        panelInsertar.add(txtNombre);
        panelInsertar.add(new JLabel("Cantidad:"));
        panelInsertar.add(txtCantidad);
        panelInsertar.add(new JLabel("Precio Unitario:"));
        panelInsertar.add(txtPrecio);
        panelInsertar.add(new JLabel("Categoría:"));
        panelInsertar.add(comboCategoria);
        panelInsertar.add(new JLabel("Marca:"));
        panelInsertar.add(comboMarca);
        panelInsertar.add(btnVolver);
        panelInsertar.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            guardarProducto(
                    txtNombre.getText(),
                    txtCantidad.getText(),
                    txtPrecio.getText(),
                    (String) comboCategoria.getSelectedItem(),
                    (String) comboMarca.getSelectedItem()
            );
            txtNombre.setText("");
            txtCantidad.setText("");
            txtPrecio.setText("");
            txtNombre.requestFocus();
        });

        btnVolver.addActionListener(e -> cardLayout.show(cardPanel, "Inicio"));
        cardPanel.add(panelInsertar, "Insertar");
    }

    private void cargarCategorias(JComboBox<String> combo) {
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nombre FROM Categoria")) {
            while (rs.next()) {
                combo.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorías: " + e.getMessage());
        }
    }

    private void cargarMarcas(JComboBox<String> combo) {
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nombre FROM Marca")) {
            while (rs.next()) {
                combo.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar marcas: " + e.getMessage());
        }
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
            String url = "jdbc:mysql://localhost:3306/Papeleria";
            String usuario = "Daniel";
            String contrasena = "pape";
            conexion = DriverManager.getConnection(url, usuario, contrasena);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void guardarProducto(String nombre, String cantidadStr, String precioStr, String nombreCategoria, String nombreMarca) {
        try {
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre no puede estar vacío", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);

            int idCategoria = obtenerIdPorNombre("Categoria", nombreCategoria);
            int idMarca = obtenerIdPorNombre("Marca", nombreMarca);

            if (idCategoria == -1 || idMarca == -1) {
                JOptionPane.showMessageDialog(this, "Categoría o Marca no válida", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO Producto (nombre, precio_venta, id_categoria, id_marca) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, nombre);
                pstmt.setDouble(2, precio);
                pstmt.setInt(3, idCategoria);
                pstmt.setInt(4, idMarca);
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idProducto = rs.getInt(1);
                    agregarLote(idProducto, cantidad, precio);
                }

                JOptionPane.showMessageDialog(this, "Producto guardado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser números", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en BD: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int obtenerIdPorNombre(String tabla, String nombre) throws SQLException {
        String sql = "SELECT id_" + tabla.toLowerCase() + " FROM " + tabla + " WHERE nombre = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    private void agregarLote(int idProducto, int cantidad, double precio) throws SQLException {
        String sql = "INSERT INTO Lote (fecha_entrada, precio_uni, cantidad, id_producto) VALUES (CURDATE(), ?, ?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setDouble(1, precio);
            pstmt.setInt(2, cantidad);
            pstmt.setInt(3, idProducto);
            pstmt.executeUpdate();
        }
    }

    private void actualizarTablaProductos() {
        try {
            modeloTabla.setRowCount(0);

            String sql = """
            SELECT p.id_producto, p.nombre, p.precio_venta, IFNULL(SUM(l.cantidad), 0) AS total_cantidad, MAX(l.fecha_entrada) AS ultima_fecha
            FROM Producto p LEFT JOIN Lote l ON p.id_producto = l.id_producto GROUP BY p.id_producto, p.nombre, p.precio_venta ORDER BY p.nombre """;

            try (PreparedStatement pstmt = conexion.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Timestamp fechaRegistro = rs.getTimestamp("ultima_fecha");
                    String fechaFormateada = (fechaRegistro != null)
                            ? dateFormat.format(fechaRegistro)
                            : "Sin fecha";

                    modeloTabla.addRow(new Object[]{
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            String.format("$%.2f", rs.getDouble("precio_venta")),
                            rs.getInt("total_cantidad"),
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
        TiendaApp app = new TiendaApp();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (app.conexion != null && !app.conexion.isClosed()) {
                    app.conexion.close();
                    System.out.println("Conexión cerrada correctamente.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));

        SwingUtilities.invokeLater(() -> app.setVisible(true));
    }
}
