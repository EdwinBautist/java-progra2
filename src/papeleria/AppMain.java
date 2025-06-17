package papeleria;

import papeleria.model.dao.CategoriaDAO;
import papeleria.model.dao.MarcaDAO;
import papeleria.model.dao.ProductoDAO;
import papeleria.model.entity.Categoria;
import papeleria.model.entity.Marca;
import papeleria.model.entity.Producto;
import papeleria.view.MainFrame;
import papeleria.view.PanelGestionMarca;
import papeleria.view.PanelInsertarProducto;
import papeleria.view.PanelMostrarProductos;

import javax.swing.*;
import java.awt.*; // Importa java.awt.* para GridBagLayout, GridBagConstraints, Insets, FlowLayout, Dimension, Font
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List; // Importa java.util.List
import papeleria.model.dao.ConexionBD;

public class AppMain {
        private MainFrame mainFrame;
        private ProductoDAO productoDAO;
        private CategoriaDAO categoriaDAO;
        private MarcaDAO marcaDAO;

        public AppMain() {
                mainFrame = new MainFrame();
                productoDAO = new ProductoDAO();
                categoriaDAO = new CategoriaDAO();
                marcaDAO = new MarcaDAO();
                setupEventListeners();
        }

        public void init() {
                mainFrame.setVisible(true);
                mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "Inicio");

                mainFrame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                                ConexionBD.closeConnection();
                        }
                });
        }

        private void setupEventListeners() {
                // --- Listeners para PanelInicio ---
                mainFrame.getPanelInicio().getBtnInsertarProducto().addActionListener(e -> {
                        mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "InsertarProducto");
                });
                mainFrame.getPanelInicio().getBtnMostrarProductos().addActionListener(e -> {
                        mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "MostrarProductos");
                });
                mainFrame.getPanelInicio().getBtnGestionMarca().addActionListener(e -> {
                        mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "GestionMarca");
                });

                // --- Listeners para PanelInsertarProducto ---
                mainFrame.getPanelInsertar().getBtnGuardar().addActionListener(e -> guardarProducto());
                mainFrame.getPanelInsertar().getBtnVolver().addActionListener(e -> mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "Inicio"));
                mainFrame.getPanelInsertar().getBtnAgregarCategoria().addActionListener(e -> abrirDialogoAgregarCategoria());

                // --- Listeners para PanelMostrarProductos ---
                mainFrame.getPanelMostrar().getBtnBuscar().addActionListener(e -> buscarProductos());
                mainFrame.getPanelMostrar().getTxtBusqueda().addActionListener(e -> buscarProductos());
                mainFrame.getPanelMostrar().getBtnEditar().addActionListener(e -> editarProductoSeleccionado());
                mainFrame.getPanelMostrar().getBtnEliminar().addActionListener(e -> eliminarProductoSeleccionado());
                mainFrame.getPanelMostrar().getBtnVolver().addActionListener(e -> mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "Inicio"));

                // --- Listeners para PanelGestionMarca ---
                mainFrame.getPanelGestionMarca().getBtnBuscar().addActionListener(e -> buscarMarcas());
                mainFrame.getPanelGestionMarca().getTxtBusqueda().addActionListener(e -> buscarMarcas());
                mainFrame.getPanelGestionMarca().getBtnAgregar().addActionListener(e -> abrirDialogoAgregarMarca());
                mainFrame.getPanelGestionMarca().getBtnEditar().addActionListener(e -> abrirDialogoEditarMarca());
                mainFrame.getPanelGestionMarca().getBtnEliminar().addActionListener(e -> eliminarMarcaSeleccionada());
                mainFrame.getPanelGestionMarca().getBtnVolver().addActionListener(e -> mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "Inicio"));

                // --- Listener para cuando se cambia de panel en el CardLayout ---
                mainFrame.getCardPanel().addComponentListener(new java.awt.event.ComponentAdapter() {
                        @Override
                        public void componentShown(java.awt.event.ComponentEvent e) {
                                if (e.getComponent() == mainFrame.getPanelInsertar()) {
                                        cargarCombosInsertar();
                                        mainFrame.getPanelInsertar().limpiarCampos();
                                } else if (e.getComponent() == mainFrame.getPanelMostrar()) {
                                        mainFrame.getPanelMostrar().getTxtBusqueda().setText("");
                                        buscarProductos();
                                } else if (e.getComponent() == mainFrame.getPanelGestionMarca()) {
                                        mainFrame.getPanelGestionMarca().limpiarBusqueda();
                                        buscarMarcas();
                                }
                        }
                });
        }

        private void guardarProducto() {
                try {
                        String nombre = mainFrame.getPanelInsertar().getTxtNombre().getText();
                        // Verificar si el campo de cantidad está vacío antes de intentar parsear
                        String cantidadText = mainFrame.getPanelInsertar().getTxtCantidad().getText();
                        if (cantidadText.isEmpty()) {
                                JOptionPane.showMessageDialog(mainFrame, "La Cantidad no puede estar vacía.", "Validación", JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                        int cantidad = Integer.parseInt(cantidadText);

                        // Verificar si el campo de precio está vacío antes de intentar parsear
                        String precioText = mainFrame.getPanelInsertar().getTxtPrecio().getText();
                        if (precioText.isEmpty()) {
                                JOptionPane.showMessageDialog(mainFrame, "El Precio no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                        double precio = Double.parseDouble(precioText);

                        Categoria categoria = (Categoria) mainFrame.getPanelInsertar().getComboCategoria().getSelectedItem();
                        Marca marca = (Marca) mainFrame.getPanelInsertar().getComboMarca().getSelectedItem();

                        if (nombre.isEmpty() || categoria == null || marca == null) {
                                JOptionPane.showMessageDialog(mainFrame, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                        if (cantidad <= 0 || precio <= 0) {
                                JOptionPane.showMessageDialog(mainFrame, "Cantidad y Precio deben ser valores positivos.", "Validación", JOptionPane.WARNING_MESSAGE);
                                return;
                        }

                        Producto nuevoProducto = new Producto(nombre, precio, categoria, marca);
                        productoDAO.insertarProducto(nuevoProducto, cantidad);
                        JOptionPane.showMessageDialog(mainFrame, "Producto y lote inicial guardados con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        mainFrame.getPanelInsertar().limpiarCampos();
                } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(mainFrame, "Por favor, introduce valores numéricos válidos para Cantidad y Precio.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(mainFrame, "Error al guardar el producto: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                }
        }

        private void buscarProductos() {
                try {
                        String filtro = mainFrame.getPanelMostrar().getTxtBusqueda().getText();
                        List<Producto> productos = productoDAO.buscarProductos(filtro);
                        mainFrame.getPanelMostrar().actualizarTabla(productos);
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(mainFrame, "Error al buscar productos: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                }
        }

        private void editarProductoSeleccionado() {
                int idProducto = mainFrame.getPanelMostrar().getProductoSeleccionadoId();
                if (idProducto == -1) {
                        JOptionPane.showMessageDialog(mainFrame, "Por favor, selecciona un producto para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                }

                try {
                        Producto producto = productoDAO.obtenerProductoPorId(idProducto);
                        if (producto != null) {
                                JDialog dialog = new JDialog(mainFrame, "Editar Producto", true);
                                dialog.setSize(400, 300);
                                dialog.setLocationRelativeTo(mainFrame);
                                dialog.setLayout(new GridBagLayout());
                                JPanel panel = (JPanel) dialog.getContentPane();
                                panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                                GridBagConstraints gbc = new GridBagConstraints();
                                gbc.insets = new Insets(5, 5, 5, 5);
                                gbc.fill = GridBagConstraints.HORIZONTAL;

                                // Campos para editar
                                JLabel lblNombre = new JLabel("Nombre:");
                                JTextField txtNombre = new JTextField(producto.getNombre(), 20);
                                JLabel lblPrecio = new JLabel("Precio Venta:");
                                JTextField txtPrecio = new JTextField(String.valueOf(producto.getPrecioVenta()), 20);
                                JLabel lblCategoria = new JLabel("Categoría:");
                                JComboBox<Categoria> comboCategoria = new JComboBox<>();
                                cargarCategoriasEnCombo(comboCategoria);
                                comboCategoria.setSelectedItem(producto.getCategoria());

                                JLabel lblMarca = new JLabel("Marca:");
                                JComboBox<Marca> comboMarca = new JComboBox<>();
                                cargarMarcasEnCombo(comboMarca);
                                comboMarca.setSelectedItem(producto.getMarca());

                                gbc.gridx = 0; gbc.gridy = 0; dialog.add(lblNombre, gbc);
                                gbc.gridx = 1; gbc.gridy = 0; dialog.add(txtNombre, gbc);
                                gbc.gridx = 0; gbc.gridy = 1; dialog.add(lblPrecio, gbc);
                                gbc.gridx = 1; gbc.gridy = 1; dialog.add(txtPrecio, gbc);
                                gbc.gridx = 0; gbc.gridy = 2; dialog.add(lblCategoria, gbc);
                                gbc.gridx = 1; gbc.gridy = 2; dialog.add(comboCategoria, gbc);
                                gbc.gridx = 0; gbc.gridy = 3; dialog.add(lblMarca, gbc);
                                gbc.gridx = 1; gbc.gridy = 3; dialog.add(comboMarca, gbc);

                                JButton btnGuardar = new JButton("Guardar Cambios");
                                JButton btnCancelar = new JButton("Cancelar");

                                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
                                buttonPanel.add(btnGuardar);
                                buttonPanel.add(btnCancelar);

                                gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; dialog.add(buttonPanel, gbc);

                                btnGuardar.addActionListener(e -> {
                                        try {
                                                String nuevoNombre = txtNombre.getText().trim();
                                                double nuevoPrecio = Double.parseDouble(txtPrecio.getText().trim());
                                                Categoria nuevaCategoria = (Categoria) comboCategoria.getSelectedItem();
                                                Marca nuevaMarca = (Marca) comboMarca.getSelectedItem();

                                                if (nuevoNombre.isEmpty() || nuevaCategoria == null || nuevaMarca == null) {
                                                        JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                                                        return;
                                                }
                                                if (nuevoPrecio <= 0) {
                                                        JOptionPane.showMessageDialog(dialog, "El precio debe ser un valor positivo.", "Validación", JOptionPane.WARNING_MESSAGE);
                                                        return;
                                                }

                                                producto.setNombre(nuevoNombre);
                                                producto.setPrecioVenta(nuevoPrecio);
                                                producto.setCategoria(nuevaCategoria);
                                                producto.setMarca(nuevaMarca);

                                                productoDAO.actualizarProducto(producto);
                                                JOptionPane.showMessageDialog(dialog, "Producto actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                                dialog.dispose();
                                                buscarProductos();
                                        } catch (NumberFormatException ex) {
                                                JOptionPane.showMessageDialog(dialog, "Introduce un precio válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                                        } catch (SQLException ex) {
                                                JOptionPane.showMessageDialog(dialog, "Error al actualizar el producto: " + ex.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
                                                ex.printStackTrace();
                                        }
                                });

                                btnCancelar.addActionListener(e -> dialog.dispose());
                                dialog.setVisible(true);

                        } else {
                                JOptionPane.showMessageDialog(mainFrame, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(mainFrame, "Error al obtener detalles del producto: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                }
        }

        private void eliminarProductoSeleccionado() {
                int idProducto = mainFrame.getPanelMostrar().getProductoSeleccionadoId();
                if (idProducto == -1) {
                        JOptionPane.showMessageDialog(mainFrame, "Por favor, selecciona un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                }

                int confirm = JOptionPane.showConfirmDialog(mainFrame, "¿Estás seguro de que quieres eliminar este producto? Se eliminarán también sus lotes asociados.", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                        try {
                                productoDAO.eliminarProducto(idProducto);
                                JOptionPane.showMessageDialog(mainFrame, "Producto eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                buscarProductos();
                        } catch (SQLException e) {
                                JOptionPane.showMessageDialog(mainFrame, "Error al eliminar el producto: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
                                e.printStackTrace();
                        }
                }
        }

        private void cargarCombosInsertar() {
                try {
                        List<Categoria> categorias = categoriaDAO.obtenerTodasCategorias();
                        mainFrame.getPanelInsertar().cargarCategorias(categorias);

                        List<Marca> marcas = marcaDAO.obtenerTodasMarcas();
                        mainFrame.getPanelInsertar().cargarMarcas(marcas);
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(mainFrame, "Error al cargar categorías o marcas: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                }
        }

        private void cargarCategoriasEnCombo(JComboBox<Categoria> combo) throws SQLException {
                combo.removeAllItems();
                List<Categoria> categorias = categoriaDAO.obtenerTodasCategorias();
                for (Categoria cat : categorias) {
                        combo.addItem(cat);
                }
        }

        private void cargarMarcasEnCombo(JComboBox<Marca> combo) throws SQLException {
                combo.removeAllItems();
                List<Marca> marcas = marcaDAO.obtenerTodasMarcas();
                for (Marca mar : marcas) {
                        combo.addItem(mar);
                }
        }

        private void abrirDialogoAgregarCategoria() {
                JDialog dialog = new JDialog(mainFrame, "Agregar Nueva Categoría", true);
                dialog.setSize(350, 180);
                dialog.setLocationRelativeTo(mainFrame);
                dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
                ((JPanel) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JLabel lblNombre = new JLabel("Nombre de la Categoría:");
                JTextField txtNombre = new JTextField(20);
                JButton btnGuardar = new JButton("Guardar");
                JButton btnCancelar = new JButton("Cancelar");

                btnGuardar.addActionListener(e -> {
                        String nombreCategoria = txtNombre.getText().trim();
                        if (nombreCategoria.isEmpty()) {
                                JOptionPane.showMessageDialog(dialog, "El nombre de la categoría no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                        try {
                                // Aquí, el ID se establecerá en 0, la BD lo asignará si AUTO_INCREMENT está configurado
                                Categoria nuevaCategoria = new Categoria(0, nombreCategoria);
                                categoriaDAO.insertarCategoria(nuevaCategoria);
                                JOptionPane.showMessageDialog(dialog, "Categoría '" + nombreCategoria + "' agregada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                dialog.dispose();
                                cargarCombosInsertar();
                        } catch (SQLException ex) {
                                if (ex.getSQLState().equals("23000") || ex.getSQLState().startsWith("23")) {
                                        JOptionPane.showMessageDialog(dialog, "Error: Ya existe una categoría con ese nombre o hay un problema de integridad de datos.", "Error de Duplicado/Integridad", JOptionPane.ERROR_MESSAGE);
                                } else {
                                        JOptionPane.showMessageDialog(dialog, "Error al agregar categoría: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                                        ex.printStackTrace();
                                }
                        }
                });

                btnCancelar.addActionListener(e -> dialog.dispose());

                dialog.add(lblNombre);
                dialog.add(txtNombre);
                dialog.add(btnGuardar);
                dialog.add(btnCancelar);

                dialog.setVisible(true);
        }

        // --- Métodos de Lógica para Marcas ---

        private void buscarMarcas() {
                try {
                        String filtro = mainFrame.getPanelGestionMarca().getTxtBusqueda().getText();
                        List<Marca> marcas = marcaDAO.obtenerTodasMarcas();
                        if (!filtro.isEmpty()) {
                                marcas.removeIf(marca -> !marca.getNombre().toLowerCase().contains(filtro.toLowerCase()));
                        }
                        mainFrame.getPanelGestionMarca().actualizarTabla(marcas);
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(mainFrame, "Error al buscar marcas: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                }
        }

        private void abrirDialogoAgregarMarca() {
                JDialog dialog = new JDialog(mainFrame, "Agregar Nueva Marca", true);
                dialog.setSize(350, 180);
                dialog.setLocationRelativeTo(mainFrame);
                dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
                ((JPanel) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JLabel lblNombre = new JLabel("Nombre de la Marca:");
                JTextField txtNombre = new JTextField(20);
                JButton btnGuardar = new JButton("Guardar");
                JButton btnCancelar = new JButton("Cancelar");

                btnGuardar.addActionListener(e -> {
                        String nombreMarca = txtNombre.getText().trim();
                        if (nombreMarca.isEmpty()) {
                                JOptionPane.showMessageDialog(dialog, "El nombre de la marca no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                        try {
                                Marca nuevaMarca = new Marca(0, nombreMarca);
                                marcaDAO.insertarMarca(nuevaMarca);
                                JOptionPane.showMessageDialog(dialog, "Marca '" + nombreMarca + "' agregada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                dialog.dispose();
                                buscarMarcas();
                                cargarCombosInsertar();
                        } catch (SQLException ex) {
                                if (ex.getSQLState().equals("23000") || ex.getSQLState().startsWith("23")) {
                                        JOptionPane.showMessageDialog(dialog, "Error: Ya existe una marca con ese nombre o hay un problema de integridad de datos.", "Error de Duplicado/Integridad", JOptionPane.ERROR_MESSAGE);
                                } else {
                                        JOptionPane.showMessageDialog(dialog, "Error al agregar marca: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                                        ex.printStackTrace();
                                }
                        }
                });

                btnCancelar.addActionListener(e -> dialog.dispose());

                dialog.add(lblNombre);
                dialog.add(txtNombre);
                dialog.add(btnGuardar);
                dialog.add(btnCancelar);

                dialog.setVisible(true);
        }

        private void abrirDialogoEditarMarca() {
                int idMarca = mainFrame.getPanelGestionMarca().getMarcaSeleccionadaId();
                if (idMarca == -1) {
                        JOptionPane.showMessageDialog(mainFrame, "Por favor, selecciona una marca para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                }

                try {
                        Marca marcaAEditar = marcaDAO.obtenerMarcaPorId(idMarca);
                        if (marcaAEditar == null) {
                                JOptionPane.showMessageDialog(mainFrame, "Marca no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                        }

                        JDialog dialog = new JDialog(mainFrame, "Editar Marca", true);
                        dialog.setSize(350, 180);
                        dialog.setLocationRelativeTo(mainFrame);
                        dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
                        ((JPanel) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                        JLabel lblNombre = new JLabel("Nuevo Nombre:");
                        JTextField txtNombre = new JTextField(marcaAEditar.getNombre(), 20);
                        JButton btnGuardar = new JButton("Guardar Cambios");
                        JButton btnCancelar = new JButton("Cancelar");

                        btnGuardar.addActionListener(e -> {
                                String nuevoNombre = txtNombre.getText().trim();
                                if (nuevoNombre.isEmpty()) {
                                        JOptionPane.showMessageDialog(dialog, "El nombre de la marca no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                                        return;
                                }
                                try {
                                        marcaAEditar.setNombre(nuevoNombre);
                                        marcaDAO.actualizarMarca(marcaAEditar);
                                        JOptionPane.showMessageDialog(dialog, "Marca actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                        dialog.dispose();
                                        buscarMarcas();
                                        cargarCombosInsertar();
                                } catch (SQLException ex) {
                                        if (ex.getSQLState().equals("23000") || ex.getSQLState().startsWith("23")) {
                                                JOptionPane.showMessageDialog(dialog, "Error: Ya existe una marca con ese nombre o hay un problema de integridad de datos.", "Error de Duplicado/Integridad", JOptionPane.ERROR_MESSAGE);
                                        } else {
                                                JOptionPane.showMessageDialog(dialog, "Error al actualizar marca: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                                                ex.printStackTrace();
                                        }
                                }
                        });

                        btnCancelar.addActionListener(e -> dialog.dispose());

                        dialog.add(lblNombre);
                        dialog.add(txtNombre);
                        dialog.add(btnGuardar);
                        dialog.add(btnCancelar);

                        dialog.setVisible(true);

                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(mainFrame, "Error al obtener detalles de la marca: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                }
        }

        private void eliminarMarcaSeleccionada() {
                int idMarca = mainFrame.getPanelGestionMarca().getMarcaSeleccionadaId();
                if (idMarca == -1) {
                        JOptionPane.showMessageDialog(mainFrame, "Por favor, selecciona una marca para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                }

                int confirm = JOptionPane.showConfirmDialog(mainFrame, "¿Estás seguro de que quieres eliminar esta marca? Esto podría afectar a productos asociados.", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                        try {
                                marcaDAO.eliminarMarca(idMarca);
                                JOptionPane.showMessageDialog(mainFrame, "Marca eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                buscarMarcas();
                                cargarCombosInsertar();
                        } catch (SQLException e) {
                                if (e.getSQLState().startsWith("23")) {
                                        JOptionPane.showMessageDialog(mainFrame, "No se puede eliminar la marca porque hay productos asociados a ella. Primero elimina o reasigna los productos.", "Error de Integridad", JOptionPane.ERROR_MESSAGE);
                                } else {
                                        JOptionPane.showMessageDialog(mainFrame, "Error al eliminar la marca: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
                                        e.printStackTrace();
                                }
                        }
                }
        }

        public static void main(String[] args) {
                SwingUtilities.invokeLater(() -> {
                        AppMain app = new AppMain();
                        app.init();
                });
        }
}