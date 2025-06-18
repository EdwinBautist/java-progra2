package papeleria.view;

import papeleria.model.entity.Categoria;
import papeleria.model.entity.Marca;
import javax.swing.*;
import java.awt.*;

public class PanelInsertarProducto extends JPanel {
    private JTextField txtNombre, txtCantidad, txtPrecio;
    private JComboBox<Categoria> comboCategoria;
    private JComboBox<Marca> comboMarca;
    private JButton btnGuardar, btnVolver;

    public PanelInsertarProducto() {
        setLayout(new GridLayout(6, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campos del formulario
        add(new JLabel("Nombre del Producto:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        add(txtCantidad);

        add(new JLabel("Precio Unitario:"));
        txtPrecio = new JTextField();
        add(txtPrecio);

        add(new JLabel("Categoría:"));
        comboCategoria = new JComboBox<>();
        add(comboCategoria);

        add(new JLabel("Marca:"));
        comboMarca = new JComboBox<>();
        add(comboMarca);

        // Botones
        btnVolver = new JButton("Volver");
        add(btnVolver);

        btnGuardar = new JButton("Guardar");
        add(btnGuardar);
    }

    // Getters actualizados
    public JComboBox<Categoria> getComboCategoria() {
        return comboCategoria;
    }

    public JComboBox<Marca> getComboMarca() {
        return comboMarca;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtCantidad() {
        return txtCantidad;
    }

    public JTextField getTxtPrecio() {
        return txtPrecio;
    }
}

