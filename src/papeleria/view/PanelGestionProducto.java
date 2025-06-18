package papeleria.view;

import papeleria.model.entity.Categoria;
import papeleria.model.entity.Marca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGestionProducto extends JPanel {
    private JTextField txtNombre, txtPrecio, txtCantidad;
    private JComboBox<Categoria> comboCategoria;
    private JComboBox<Marca> comboMarca;
    private JButton btnGuardar, btnActualizar, btnVolver;
    private JTable tablaProductos;

    public PanelGestionProducto() {
        setLayout(new BorderLayout());

        // Formulario superior
        JPanel formulario = new JPanel(new GridLayout(6, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtCantidad = new JTextField();
        comboCategoria = new JComboBox<>();
        comboMarca = new JComboBox<>();

        btnGuardar = new JButton("Guardar Producto");
        btnActualizar = new JButton("Actualizar Tabla");

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Precio:"));
        formulario.add(txtPrecio);
        formulario.add(new JLabel("Cantidad:"));
        formulario.add(txtCantidad);
        formulario.add(new JLabel("Categoría:"));
        formulario.add(comboCategoria);
        formulario.add(new JLabel("Marca:"));
        formulario.add(comboMarca);
        formulario.add(btnGuardar);
        formulario.add(btnActualizar);

        // Tabla de productos
        tablaProductos = new JTable();
        JScrollPane scroll = new JScrollPane(tablaProductos);

        // Botón inferior
        btnVolver = new JButton("Volver");
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnVolver);

        // Agregar todo al panel
        add(formulario, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    // Getters para AppMain
    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtPrecio() {
        return txtPrecio;
    }

    public JTextField getTxtCantidad() {
        return txtCantidad;
    }

    public JComboBox<Categoria> getComboCategoria() {
        return comboCategoria;
    }

    public JComboBox<Marca> getComboMarca() {
        return comboMarca;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public JTable getTablaProductos() {
        return tablaProductos;
    }

    public void cargarCategorias(List<Categoria> categorias) {
        comboCategoria.removeAllItems();
        for (Categoria c : categorias) {
            comboCategoria.addItem(c);
        }
    }

    public void cargarMarcas(List<Marca> marcas) {
        comboMarca.removeAllItems();
        for (Marca m : marcas) {
            comboMarca.addItem(m);
        }
    }
}
