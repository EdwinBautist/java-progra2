package papeleria.view;

import papeleria.model.entity.Producto; // Importa tu clase Producto
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PanelMostrarProductos extends JPanel {
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnVolver;

    public PanelMostrarProductos() {
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas de la tabla no son editables directamente
            }
        };
        // Define las columnas de la tabla
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Precio Venta"); // Cambié el nombre de la columna para mayor claridad
        modeloTabla.addColumn("Cantidad (Stock)"); // Cambié el nombre de la columna para mayor claridad
        modeloTabla.addColumn("Fecha Último Lote");

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setAutoCreateRowSorter(true); // Permite ordenar al hacer clic en las cabeceras
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        // Panel superior para búsqueda y botones de acción
        JPanel panelAcciones = new JPanel(new BorderLayout());

        // Sub-panel para la búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBusqueda = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(new JLabel("Buscar por Nombre:"));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        panelAcciones.add(panelBusqueda, BorderLayout.NORTH);

        // Sub-panel para los botones de acción (Editar, Eliminar, Volver)
        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEditar = new JButton("Editar Producto");
        btnEliminar = new JButton("Eliminar Producto");
        btnVolver = new JButton("Volver al Inicio");

        panelBotonesAccion.add(btnEditar);
        panelBotonesAccion.add(btnEliminar);
        panelBotonesAccion.add(btnVolver);
        panelAcciones.add(panelBotonesAccion, BorderLayout.CENTER);

        // Añade los paneles al panel principal de la vista
        add(panelAcciones, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Getters para que la clase principal (o controlador) pueda acceder a los componentes
    public JTable getTablaProductos() { return tablaProductos; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JTextField getTxtBusqueda() { return txtBusqueda; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnEditar() { return btnEditar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnVolver() { return btnVolver; }

    /**
     * Actualiza la tabla con una nueva lista de productos.
     * @param productos Lista de objetos Producto a mostrar.
     */
    public void actualizarTabla(List<Producto> productos) {
        modeloTabla.setRowCount(0); // Limpiar todas las filas existentes en la tabla
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Producto p : productos) {
            String fechaFormateada = (p.getUltimaFechaRegistro() != null)
                    ? dateFormat.format(new Date(p.getUltimaFechaRegistro().getTime()))
                    : "Sin fecha"; // Si no hay fecha (ej. no hay lotes aún)
            modeloTabla.addRow(new Object[]{
                    p.getIdProducto(),
                    p.getNombre(),
                    String.format("$%.2f", p.getPrecioVenta()), // Formatea el precio
                    p.getStockTotal(),
                    fechaFormateada
            });
        }
    }
}