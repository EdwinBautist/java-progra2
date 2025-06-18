package papeleria.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class PanelProductos extends JPanel {
    private JTable tablaProductos;
    private JButton btnInsertar, btnActualizar, btnVolver;

    public PanelProductos() {
        setLayout(new BorderLayout());

        // Tabla de productos
        tablaProductos = new JTable();
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnInsertar = new JButton("Insertar Producto");
        btnActualizar = new JButton("Actualizar Lista");
        btnVolver = new JButton("Volver");

        panelBotones.add(btnInsertar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);
    }

    public JTable getTablaProductos() {
        return tablaProductos;
    }

    public JButton getBtnInsertar() {
        return btnInsertar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }
}