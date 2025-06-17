package papeleria.view;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de inicio que sirve como menú principal de la aplicación.
 */
public class PanelInicio extends JPanel {
    private JButton btnInsertarProducto;
    private JButton btnMostrarProductos;
    private JButton btnGestionMarca; // Nuevo botón para gestión de marcas

    public PanelInicio() {
        setLayout(new GridBagLayout()); // Usamos GridBagLayout para centrar mejor
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Espacio entre componentes
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hacer que los botones llenen el espacio

        // Título
        JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Papelería", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridwidth = 1;
        gbc.gridy = 0;
        add(lblTitulo, gbc);

        // Botones
        gbc.gridy = 1;
        btnInsertarProducto = new JButton("Registrar Nuevo Producto");
        btnInsertarProducto.setPreferredSize(new Dimension(250, 50)); // Tamaño fijo para uniformidad
        add(btnInsertarProducto, gbc);

        gbc.gridy = 2;
        btnMostrarProductos = new JButton("Gestionar Productos");
        btnMostrarProductos.setPreferredSize(new Dimension(250, 50));
        add(btnMostrarProductos, gbc);

        gbc.gridy = 3;
        btnGestionMarca = new JButton("Gestionar Marcas"); // Inicializar el nuevo botón
        btnGestionMarca.setPreferredSize(new Dimension(250, 50));
        add(btnGestionMarca, gbc); // Añadir el nuevo botón

        // Puedes añadir más botones aquí para otras gestiones (clientes, empleados, lotes, ventas, etc.)
    }

    // Getters para que AppMain pueda añadir los Listeners
    public JButton getBtnInsertarProducto() {
        return btnInsertarProducto;
    }

    public JButton getBtnMostrarProductos() {
        return btnMostrarProductos;
    }

    public JButton getBtnGestionMarca() {
        return btnGestionMarca;
    }
}