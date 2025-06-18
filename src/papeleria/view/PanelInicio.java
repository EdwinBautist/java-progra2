package papeleria.view;

import javax.swing.*;
import java.awt.*;

public class PanelInicio extends JPanel {
    private JButton btnGestionarProductos;
    private JButton btnGestionMarca;
    private JButton btnGestionCategorias;
    private JButton btnGestionClientes;
    private JButton btnRealizarVenta;

    public PanelInicio() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        JLabel lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE PAPELERÍA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 102, 204));
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;

        gbc.gridy = 1;
        btnGestionarProductos = createStyledButton("Gestionar Productos", buttonFont);
        add(btnGestionarProductos, gbc);

        gbc.gridy = 2;
        btnGestionMarca = createStyledButton("Gestionar Marcas", buttonFont);
        add(btnGestionMarca, gbc);

        gbc.gridy = 3;
        btnGestionCategorias = createStyledButton("Gestionar Categorías", buttonFont);
        add(btnGestionCategorias, gbc);

        gbc.gridy = 4;
        btnGestionClientes = createStyledButton("Gestionar Clientes", buttonFont);
        add(btnGestionClientes, gbc);

        gbc.gridy = 5;
        btnRealizarVenta = createStyledButton("Realizar Venta", buttonFont);
        btnRealizarVenta.setBackground(new Color(76, 175, 80));
        btnRealizarVenta.setForeground(Color.WHITE);
        add(btnRealizarVenta, gbc);
    }

    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(new Color(66, 165, 245));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    // Getters
    public JButton getBtnGestionarProductos() {
        return btnGestionarProductos;
    }

    public JButton getBtnGestionMarca() {
        return btnGestionMarca;
    }

    public JButton getBtnGestionCategorias() {
        return btnGestionCategorias;
    }

    public JButton getBtnGestionClientes() {
        return btnGestionClientes;
    }

    public JButton getBtnRealizarVenta() {
        return btnRealizarVenta;
    }
}