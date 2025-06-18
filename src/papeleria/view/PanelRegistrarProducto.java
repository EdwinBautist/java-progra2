package papeleria.view;

import javax.swing.*;
import java.awt.*;

public class PanelRegistrarProducto extends JPanel {
    private JTextField txtNombre, txtPrecio, txtStock;
    private JComboBox<String> comboMarca, comboCategoria;
    private JButton btnGuardar, btnVolver;

    public PanelRegistrarProducto() {
        setLayout(new GridLayout(6, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        add(txtPrecio);

        add(new JLabel("Stock:"));
        txtStock = new JTextField();
        add(txtStock);

        add(new JLabel("Marca:"));
        comboMarca = new JComboBox<>();
        add(comboMarca);

        add(new JLabel("Categoría:"));
        comboCategoria = new JComboBox<>();
        add(comboCategoria);

        btnGuardar = new JButton("Guardar");
        add(btnGuardar);

        btnVolver = new JButton("Volver");
        add(btnVolver);
    }

    // Getters
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public JTextField getTxtStock() { return txtStock; }
    public JComboBox<String> getComboMarca() { return comboMarca; }
    public JComboBox<String> getComboCategoria() { return comboCategoria; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnVolver() { return btnVolver; }
}