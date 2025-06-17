package papeleria.view;

import papeleria.model.entity.Categoria;
import papeleria.model.entity.Marca;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelInsertarProducto extends JPanel {
    private JTextField txtNombre;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JComboBox<Categoria> comboCategoria;
    private JComboBox<Marca> comboMarca;
    private JButton btnGuardar;
    private JButton btnVolver;

    public PanelInsertarProducto() {
        setLayout(new GridLayout(7, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtNombre = new JTextField();
        txtCantidad = new JTextField();
        txtPrecio = new JTextField();
        comboCategoria = new JComboBox<>();
        comboMarca = new JComboBox<>();
        btnGuardar = new JButton("Guardar Producto");
        btnVolver = new JButton("Volver al Inicio");

        add(new JLabel("Nombre del Producto:"));
        add(txtNombre);
        add(new JLabel("Cantidad (Lote Inicial):"));
        add(txtCantidad);
        add(new JLabel("Precio Venta Unitario:")); // Cambié la etiqueta para mayor claridad
        add(txtPrecio);
        add(new JLabel("Categoría:"));
        add(comboCategoria);
        add(new JLabel("Marca:"));
        add(comboMarca);
        add(btnVolver);
        add(btnGuardar);
    }

    // Getters para que la clase principal (o controlador) pueda acceder a los componentes
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtCantidad() { return txtCantidad; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public JComboBox<Categoria> getComboCategoria() { return comboCategoria; }
    public JComboBox<Marca> getComboMarca() { return comboMarca; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnVolver() { return btnVolver; }

    /**
     * Carga las categorías en el JComboBox.
     * @param categorias Lista de objetos Categoria.
     */
    public void cargarCategorias(List<Categoria> categorias) {
        comboCategoria.removeAllItems();
        for (Categoria cat : categorias) {
            comboCategoria.addItem(cat);
        }
        if (!categorias.isEmpty()) {
            comboCategoria.setSelectedIndex(0); // Seleccionar el primero por defecto
        }
    }

    /**
     * Carga las marcas en el JComboBox.
     * @param marcas Lista de objetos Marca.
     */
    public void cargarMarcas(List<Marca> marcas) {
        comboMarca.removeAllItems();
        for (Marca mar : marcas) {
            comboMarca.addItem(mar);
        }
        if (!marcas.isEmpty()) {
            comboMarca.setSelectedIndex(0); // Seleccionar el primero por defecto
        }
    }

    /**
     * Limpia los campos de texto y restablece los JComboBox a su estado inicial.
     */
    public void limpiarCampos() {
        txtNombre.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        // Seleccionar el primer item si hay alguno
        if (comboCategoria.getItemCount() > 0) comboCategoria.setSelectedIndex(0);
        if (comboMarca.getItemCount() > 0) comboMarca.setSelectedIndex(0);
        txtNombre.requestFocus(); // Pone el foco en el campo nombre
    }
}