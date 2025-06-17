package papeleria.view;

import papeleria.model.dao.ConexionBD; // Importa la clase de conexión

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * MainFrame es la ventana principal de la aplicación, gestionando el cambio entre paneles
 * usando CardLayout. No contiene lógica de negocio o acceso a datos directamente.
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private PanelInsertarProducto panelInsertar;
    private PanelMostrarProductos panelMostrar;
    private JPanel panelInicial;

    public MainFrame() {
        setTitle("Papelería La Nota - Sistema de Gestión");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Panel de inicio con los botones principales
        panelInicial = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton btnInsertar = new JButton("Insertar Producto");
        JButton btnMostrar = new JButton("Mostrar Productos");
        panelInicial.add(btnInsertar);
        panelInicial.add(btnMostrar);

        // Inicializa los paneles de las vistas
        panelInsertar = new PanelInsertarProducto();
        panelMostrar = new PanelMostrarProductos();

        // Añade los paneles al CardPanel con un nombre identificador
        cardPanel.add(panelInicial, "Inicio");
        cardPanel.add(panelInsertar, "Insertar");
        cardPanel.add(panelMostrar, "Mostrar");

        add(cardPanel, BorderLayout.CENTER); // Añade el cardPanel a la ventana principal

        // Configura los listeners para los botones del panel inicial
        // La lógica de cambiar de vista es manejada directamente aquí por MainFrame
        btnInsertar.addActionListener(e -> cardLayout.show(cardPanel, "Insertar"));
        btnMostrar.addActionListener(e -> cardLayout.show(cardPanel, "Mostrar"));

        // Asegúrate de que la conexión a la base de datos se cierre al salir de la aplicación
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConexionBD.closeConnection();
            }
        });
    }

    // Getters para que la clase AppMain pueda acceder a los paneles y controlar sus acciones
    public CardLayout getCardLayout() { return cardLayout; }
    public JPanel getCardPanel() { return cardPanel; }
    public PanelInsertarProducto getPanelInsertar() { return panelInsertar; }
    public PanelMostrarProductos getPanelMostrar() { return panelMostrar; }

    // El método main ahora solo inicializa MainFrame y maneja la conexión inicial a la BD.
    // La lógica de negocio y eventos estará en AppMain.
    // Este método main en MainFrame.java es solo para demostración, el principal real está en AppMain.java
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Intenta conectar a la BD al inicio
                ConexionBD.getConnection();
                MainFrame app = new MainFrame();
                app.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos o inicialización: " + e.getMessage(),
                        "Error Fatal", JOptionPane.ERROR_MESSAGE);
                System.exit(1); // Salir si no se puede conectar
            }
        });
    }
}