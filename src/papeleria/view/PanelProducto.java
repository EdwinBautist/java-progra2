package papeleria.view;

import javax.swing.*;
import java.awt.*;

public class PanelProducto extends JPanel {
    private JTable tabla;
    private JButton btnNuevo, btnEditar, btnEliminar, btnVolver;

    public PanelProducto() {
        setLayout(new BorderLayout());

        tabla = new JTable();
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnNuevo = new JButton("Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnVolver = new JButton("Volver");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);
    }

    public JTable getTabla() { return tabla; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnEditar() { return btnEditar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnVolver() { return btnVolver; }
}