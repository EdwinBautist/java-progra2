package papeleria.view;

import papeleria.model.entity.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelVenta extends JPanel {
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JTable tablaProductos;
    private JTable tablaVenta;
    private JButton btnAnadir;
    private JButton btnQuitar;
    private JButton btnConfirmarVenta;
    private JButton btnVolver;
    private JSpinner spinnerCantidad;
    private JComboBox<Cliente> comboClientes;

    public PanelVenta() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel superior - Búsqueda y cliente
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1, 5, 5));

        // Panel de cliente
        JPanel panelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelCliente.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        comboClientes = new JComboBox<>();
        comboClientes.setPreferredSize(new Dimension(300, 25));
        panelCliente.add(new JLabel("Seleccionar Cliente:"));
        panelCliente.add(comboClientes);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar Productos"));
        txtBusqueda = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(100, 25));

        JPanel panelBusquedaControles = new JPanel();
        panelBusquedaControles.add(new JLabel("Nombre:"));
        panelBusquedaControles.add(txtBusqueda);
        panelBusquedaControles.add(btnBuscar);

        panelBusqueda.add(panelBusquedaControles, BorderLayout.CENTER);
        panelSuperior.add(panelCliente);
        panelSuperior.add(panelBusqueda);

        // Tabla de productos encontrados
        tablaProductos = new JTable();
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTablaProductos = new JScrollPane(tablaProductos);
        scrollTablaProductos.setBorder(BorderFactory.createTitledBorder("Productos Disponibles"));
        scrollTablaProductos.setPreferredSize(new Dimension(800, 200));

        // Panel para añadir productos
        JPanel panelAnadir = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinnerCantidad.setPreferredSize(new Dimension(60, 25));
        btnAnadir = new JButton("Añadir a Venta");
        btnAnadir.setPreferredSize(new Dimension(150, 30));

        panelAnadir.add(new JLabel("Cantidad:"));
        panelAnadir.add(spinnerCantidad);
        panelAnadir.add(btnAnadir);

        // Tabla de productos en venta
        tablaVenta = new JTable();
        JScrollPane scrollTablaVenta = new JScrollPane(tablaVenta);
        scrollTablaVenta.setBorder(BorderFactory.createTitledBorder("Productos en Venta"));
        scrollTablaVenta.setPreferredSize(new Dimension(800, 200));

        // Botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnQuitar = new JButton("Quitar Seleccionado");
        btnQuitar.setPreferredSize(new Dimension(150, 35));
        btnConfirmarVenta = new JButton("Confirmar Venta");
        btnConfirmarVenta.setPreferredSize(new Dimension(150, 35));
        btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(100, 35));

        panelBotones.add(btnQuitar);
        panelBotones.add(btnConfirmarVenta);
        panelBotones.add(btnVolver);

        // Organizar componentes
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.add(panelSuperior);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(scrollTablaProductos);
        panelContenido.add(Box.createVerticalStrut(5));
        panelContenido.add(panelAnadir);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(scrollTablaVenta);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(panelBotones);

        add(panelContenido, BorderLayout.CENTER);
    }

    // Getters
    public JTextField getTxtBusqueda() { return txtBusqueda; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JTable getTablaProductos() { return tablaProductos; }
    public JTable getTablaVenta() { return tablaVenta; }
    public JButton getBtnAnadir() { return btnAnadir; }
    public JButton getBtnQuitar() { return btnQuitar; }
    public JButton getBtnConfirmarVenta() { return btnConfirmarVenta; }
    public JButton getBtnVolver() { return btnVolver; }
    public JSpinner getSpinnerCantidad() { return spinnerCantidad; }
    public JComboBox<Cliente> getComboClientes() { return comboClientes; }

    public void cargarClientes(List<Cliente> clientes) {
        comboClientes.removeAllItems();
        comboClientes.addItem(new Cliente(0, "Cliente Genérico", "", ""));
        for (Cliente c : clientes) {
            comboClientes.addItem(c);
        }
    }
}