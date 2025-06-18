package papeleria.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private PanelInicio panelInicio;
    private PanelVenta panelVenta;
    private PanelProductos panelProductos;
    private PanelInsertarProducto panelInsertarProducto;

    public MainFrame() {
        setTitle("Sistema de Papelería");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Crear paneles
        panelInicio = new PanelInicio();
        panelVenta = new PanelVenta();
        panelProductos = new PanelProductos();
        panelInsertarProducto = new PanelInsertarProducto();

        // Agregar paneles al cardPanel
        cardPanel.add(panelInicio, "Inicio");
        cardPanel.add(panelVenta, "Venta");
        cardPanel.add(panelProductos, "Productos");
        cardPanel.add(panelInsertarProducto, "InsertarProducto");

        add(cardPanel);
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public PanelInicio getPanelInicio() {
        return panelInicio;
    }

    public PanelVenta getPanelVenta() {
        return panelVenta;
    }

    public PanelProductos getPanelProductos() {
        return panelProductos;
    }

    public PanelInsertarProducto getPanelInsertarProducto() {
        return panelInsertarProducto;
    }
}