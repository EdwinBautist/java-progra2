package papeleria.view;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de inicio que sirve como menú principal de la aplicación.
 */
public class PanelInicio extends JPanel {
    private JButton btnInsertarProducto;
    private JButton btnMostrarProductos;
    private JButton btnGestionMarca;
    private JButton btnRealizarVenta;
    private JButton btnGestionClientes; // Nuevo botón para gestión de clientes

    public PanelInicio() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuración de fuente para los botones
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        // Título
        JLabel lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE PAPELERÍA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 102, 204)); // Azul oscuro
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        add(lblTitulo, gbc);

        // Botón Registrar Producto
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        btnInsertarProducto = createStyledButton("Registrar Producto", buttonFont);
        add(btnInsertarProducto, gbc);

        // Botón Gestionar Productos
        gbc.gridy = 2;
        btnMostrarProductos = createStyledButton("Gestionar Productos", buttonFont);
        add(btnMostrarProductos, gbc);

        // Botón Gestionar Marcas
        gbc.gridy = 3;
        btnGestionMarca = createStyledButton("Gestionar Marcas", buttonFont);
        add(btnGestionMarca, gbc);

        // Botón Gestionar Clientes (nuevo)
        gbc.gridy = 4;
        btnGestionClientes = createStyledButton("Gestionar Clientes", buttonFont);
        add(btnGestionClientes, gbc);

        // Botón Realizar Venta
        gbc.gridy = 5;
        btnRealizarVenta = createStyledButton("Realizar Venta", buttonFont);
        btnRealizarVenta.setBackground(new Color(76, 175, 80)); // Verde
        btnRealizarVenta.setForeground(Color.WHITE);
        add(btnRealizarVenta, gbc);

        // Panel vacío para centrar los botones
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        add(Box.createGlue(), gbc);
    }

    // Método auxiliar para crear botones con estilo consistente
    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(new Color(66, 165, 245)); // Azul claro
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    // Getters para los botones
    public JButton getBtnInsertarProducto() {
        return btnInsertarProducto;
    }

    public JButton getBtnMostrarProductos() {
        return btnMostrarProductos;
    }

    public JButton getBtnGestionMarca() {
        return btnGestionMarca;
    }

    public JButton getBtnRealizarVenta() {
        return btnRealizarVenta;
    }

    public JButton getBtnGestionClientes() {
        return btnGestionClientes;
    }
}