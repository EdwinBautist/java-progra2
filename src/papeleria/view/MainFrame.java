package papeleria.view;

import javax.swing.*;
import java.awt.*;

/**
 * Clase principal de la ventana (JFrame) que contiene los diferentes paneles.
 * Utiliza CardLayout para cambiar entre ellos.
 */
public class MainFrame extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private PanelInicio panelInicio;
    private PanelInsertarProducto panelInsertar;
    private PanelMostrarProductos panelMostrar;
    private PanelGestionMarca panelGestionMarca; // Nuevo panel de gestión de marcas
    private PanelVenta panelVenta; //Para la gestión de ventas


    public MainFrame() {
        setTitle("Sistema de Gestión de Papelería");
        setSize(1000, 700); // Tamaño inicial más grande
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Inicialización de los paneles
        panelInicio = new PanelInicio();
        panelInsertar = new PanelInsertarProducto();
        panelMostrar = new PanelMostrarProductos();
        panelGestionMarca = new PanelGestionMarca(); // Inicializar el nuevo panel
        panelVenta = new PanelVenta();
        cardPanel.add(panelVenta, "Venta");

        // Añadir paneles al CardLayout
        cardPanel.add(panelInicio, "Inicio");
        cardPanel.add(panelInsertar, "InsertarProducto");
        cardPanel.add(panelMostrar, "MostrarProductos");
        cardPanel.add(panelGestionMarca, "GestionMarca"); // Añadir el nuevo panel

        add(cardPanel); // Añadir el panel contenedor al JFrame
    }

    // --- Getters para acceder a los paneles y al CardLayout ---
    public PanelVenta getPanelVenta() { return panelVenta; }

    public JPanel getCardPanel() {
        return cardPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public PanelInicio getPanelInicio() {
        return panelInicio;
    }

    public PanelInsertarProducto getPanelInsertar() {
        return panelInsertar;
    }

    public PanelMostrarProductos getPanelMostrar() {
        return panelMostrar;
    }

    public PanelGestionMarca getPanelGestionMarca() {
        return panelGestionMarca;
    }
}