package papeleria.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private PanelInicio panelInicio;
    private PanelVenta panelVenta;
    private PanelCliente panelCliente;
    private PanelMarca panelMarca;
    private PanelCategoria panelCategoria;
    private PanelGestionProducto panelGestionProducto;
    private PanelRegistrarProducto panelRegistrarProducto;

    public MainFrame() {
        setTitle("Sistema de Gestión de Papelería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Crear los paneles
        panelInicio = new PanelInicio();
        panelVenta = new PanelVenta();
        panelCliente = new PanelCliente();
        panelMarca = new PanelMarca();
        panelCategoria = new PanelCategoria();
        panelGestionProducto = new PanelGestionProducto();
        panelRegistrarProducto = new PanelRegistrarProducto();

        // Agregar paneles al cardPanel con nombres identificativos
        cardPanel.add(panelInicio, "Inicio");
        cardPanel.add(panelVenta, "Venta");
        cardPanel.add(panelCliente, "Clientes");
        cardPanel.add(panelMarca, "Marcas");
        cardPanel.add(panelCategoria, "Categorias");
        cardPanel.add(panelGestionProducto, "GestionProducto");
        cardPanel.add(panelRegistrarProducto, "RegistrarProducto");

        add(cardPanel);
    }

    // Getters para los paneles
    public CardLayout getCardLayout() { return cardLayout; }
    public JPanel getCardPanel() { return cardPanel; }
    public PanelInicio getPanelInicio() { return panelInicio; }
    public PanelVenta getPanelVenta() { return panelVenta; }
    public PanelCliente getPanelCliente() { return panelCliente; }
    public PanelMarca getPanelMarca() { return panelMarca; }
    public PanelCategoria getPanelCategoria() { return panelCategoria; }
    public PanelGestionProducto getPanelGestionProducto() { return panelGestionProducto; }
    public PanelRegistrarProducto getPanelRegistrarProducto() { return panelRegistrarProducto; }
}