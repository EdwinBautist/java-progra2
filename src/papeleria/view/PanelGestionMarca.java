package papeleria.view;

import papeleria.model.entity.Marca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGestionMarca extends JPanel {
    private JTable tablaMarcas;
    private DefaultTableModel modeloTabla;
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnVolver;

    public PanelGestionMarca() {
        setLayout(new BorderLayout(10, 10)); // Espaciado
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen alrededor del panel

        // --- Panel Superior (Búsqueda y Título) ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestión de Marcas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        txtBusqueda = new JTextField(20);
        btnBuscar = new JButton("Buscar Marca");
        panelBusqueda.add(new JLabel("Nombre de Marca:"));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel Central (Tabla de Marcas) ---
        String[] columnas = {"ID", "Nombre"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables directamente en la tabla
            }
        };
        tablaMarcas = new JTable(modeloTabla);
        tablaMarcas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo una fila a la vez
        JScrollPane scrollPane = new JScrollPane(tablaMarcas);
        add(scrollPane, BorderLayout.CENTER);

        // --- Panel Inferior (Botones de Acción) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // Más espacio entre botones
        btnAgregar = new JButton("Nueva Marca");
        btnEditar = new JButton("Editar Marca");
        btnEliminar = new JButton("Eliminar Marca");
        btnVolver = new JButton("Volver al Inicio");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // --- Métodos para interactuar con el panel ---

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JTable getTablaMarcas() {
        return tablaMarcas;
    }

    public JTextField getTxtBusqueda() {
        return txtBusqueda;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    /**
     * Actualiza la tabla de marcas con una nueva lista de marcas.
     * @param marcas La lista de objetos Marca a mostrar.
     */
    public void actualizarTabla(List<Marca> marcas) {
        modeloTabla.setRowCount(0); // Limpiar filas existentes
        for (Marca marca : marcas) {
            modeloTabla.addRow(new Object[]{marca.getId(), marca.getNombre()});
        }
    }

    /**
     * Obtiene el ID de la marca seleccionada en la tabla.
     * @return El ID de la marca seleccionada, o -1 si no hay ninguna seleccionada.
     */
    public int getMarcaSeleccionadaId() {
        int selectedRow = tablaMarcas.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = tablaMarcas.convertRowIndexToModel(selectedRow);
            return (int) modeloTabla.getValueAt(modelRow, 0); // ID está en la columna 0
        }
        return -1;
    }

    /**
     * Limpia el campo de búsqueda.
     */
    public void limpiarBusqueda() {
        txtBusqueda.setText("");
    }
}