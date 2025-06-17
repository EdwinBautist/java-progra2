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
    private JButton btnAgregarCategoria; // Nuevo botón

    public PanelInsertarProducto() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espacio entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Registrar Nuevo Producto");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitulo, gbc);

        // Campos de entrada
        gbc.gridwidth = 1; // Resetea a una columna
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Cantidad (Lote Inicial):"), gbc);
        gbc.gridx = 1;
        txtCantidad = new JTextField(20);
        add(txtCantidad, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Precio Venta:"), gbc);
        gbc.gridx = 1;
        txtPrecio = new JTextField(20);
        add(txtPrecio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        comboCategoria = new JComboBox<>();
        add(comboCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1;
        comboMarca = new JComboBox<>();
        add(comboMarca, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER; // Centrar botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Panel para los botones
        btnGuardar = new JButton("Guardar Producto");
        btnVolver = new JButton("Volver al Inicio");
        btnAgregarCategoria = new JButton("Agregar Categoría"); // Inicializa el nuevo botón

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnVolver);
        buttonPanel.add(btnAgregarCategoria); // Añade el nuevo botón al panel de botones

        add(buttonPanel, gbc); // Añade el panel de botones al PanelInsertarProducto
    }

    // Getters
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtCantidad() { return txtCantidad; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public JComboBox<Categoria> getComboCategoria() { return comboCategoria; }
    public JComboBox<Marca> getComboMarca() { return comboMarca; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnVolver() { return btnVolver; }
    public JButton getBtnAgregarCategoria() { return btnAgregarCategoria; } // Nuevo getter

    public void cargarCategorias(List<Categoria> categorias) {
        comboCategoria.removeAllItems();
        for (Categoria cat : categorias) {
            comboCategoria.addItem(cat);
        }
    }

    public void cargarMarcas(List<Marca> marcas) {
        comboMarca.removeAllItems();
        for (Marca mar : marcas) {
            comboMarca.addItem(mar);
        }
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        if (comboCategoria.getItemCount() > 0) comboCategoria.setSelectedIndex(0);
        if (comboMarca.getItemCount() > 0) comboMarca.setSelectedIndex(0);
    }
}