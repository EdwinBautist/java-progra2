package papeleria.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import papeleria.model.entity.Producto;

public class PanelGestionProducto extends JPanel {
    private JTable tablaProductos;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizar, btnVolver;

    public PanelGestionProducto() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tablaProductos = new JTable();
        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de Productos"));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");
        btnVolver = new JButton("Volver");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnVolver);

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Getters
    public JTable getTablaProductos() { return tablaProductos; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnEditar() { return btnEditar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnVolver() { return btnVolver; }

    public void cargarProductos(List<Producto> productos) {
        String[] columnas = {"ID", "Nombre", "Precio Venta", "Stock"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Producto p : productos) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    String.format("$%,.2f", p.getPrecioVenta()),
                    p.getStock()
            });
        }

        tablaProductos.setModel(modelo);
    }
}
