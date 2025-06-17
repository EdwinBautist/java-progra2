package papeleria;

import papeleria.model.dao.CategoriaDAO;
import papeleria.model.dao.MarcaDAO;
import papeleria.model.dao.ProductoDAO;
import papeleria.model.dao.ConexionBD;
import papeleria.model.entity.Categoria;
import papeleria.model.entity.Marca;
import papeleria.model.entity.Producto;
import papeleria.view.MainFrame;
import papeleria.view.PanelInsertarProducto;
import papeleria.view.PanelMostrarProductos;

import javax.swing.*;
import java.awt.*; // Necesario para GridLayout en el diálogo de edición
import java.sql.SQLException;
import java.util.List;

/**
 * AppMain es la clase principal que inicia la aplicación,
 * orquesta la interacción entre la vista (MainFrame y sus paneles)
 * y el modelo (DAOs y entidades). Actúa como un controlador central.
 */
public class AppMain {

    private MainFrame mainFrame;
    private ProductoDAO productoDAO;
    private CategoriaDAO categoriaDAO;
    private MarcaDAO marcaDAO;

    public AppMain() {
        // Inicializa la vista principal
        mainFrame = new MainFrame();

        // Inicializa los objetos DAO para interactuar con la base de datos
        productoDAO = new ProductoDAO();
        categoriaDAO = new CategoriaDAO();
        marcaDAO = new MarcaDAO();

        // Configura los escuchadores de eventos para los componentes de la UI
        setupEventListeners();

        // Muestra la ventana principal
        mainFrame.setVisible(true);
    }

    /**
     * Configura los ActionListeners para todos los botones y campos de la interfaz.
     */
    private void setupEventListeners() {
        // --- Listeners para PanelInsertarProducto ---
        mainFrame.getPanelInsertar().getBtnGuardar().addActionListener(e -> guardarProducto());
        mainFrame.getPanelInsertar().getBtnVolver().addActionListener(e -> mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "Inicio"));

        // --- Listeners para PanelMostrarProductos ---
        mainFrame.getPanelMostrar().getBtnBuscar().addActionListener(e -> buscarProductos());
        // Permite buscar al presionar Enter en el campo de búsqueda
        mainFrame.getPanelMostrar().getTxtBusqueda().addActionListener(e -> buscarProductos());
        mainFrame.getPanelMostrar().getBtnEditar().addActionListener(e -> editarProductoSeleccionado());
        mainFrame.getPanelMostrar().getBtnEliminar().addActionListener(e -> eliminarProductoSeleccionado());
        mainFrame.getPanelMostrar().getBtnVolver().addActionListener(e -> mainFrame.getCardLayout().show(mainFrame.getCardPanel(), "Inicio"));

        // --- Listener para cuando se cambia de panel en el CardLayout ---
        // Este listener se usa para cargar datos o limpiar campos cuando un panel se hace visible.
        mainFrame.getCardPanel().addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                if (e.getComponent() == mainFrame.getPanelInsertar()) {
                    cargarCombosInsertar();
                    mainFrame.getPanelInsertar().limpiarCampos(); // Limpiar al mostrar la vista de inserción
                } else if (e.getComponent() == mainFrame.getPanelMostrar()) {
                    mainFrame.getPanelMostrar().getTxtBusqueda().setText(""); // Limpiar búsqueda al volver
                    buscarProductos(); // Cargar todos los productos al mostrar la vista de mostrar
                }
            }
        });
    }

    /**
     * Carga las categorías y marcas en los JComboBox del panel de inserción.
     */
    private void cargarCombosInsertar() {
        try {
            List<Categoria> categorias = categoriaDAO.obtenerTodasCategorias();
            mainFrame.getPanelInsertar().cargarCategorias(categorias);

            List<Marca> marcas = marcaDAO.obtenerTodasMarcas();
            mainFrame.getPanelInsertar().cargarMarcas(marcas);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error al cargar categorías o marcas: " + e.getMessage(), "Error de Datos", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Lógica para guardar un nuevo producto. Obtiene datos de la UI, valida y llama al DAO.
     */
    private void guardarProducto() {
        String nombre = mainFrame.getPanelInsertar().getTxtNombre().getText().trim();
        String cantidadStr = mainFrame.getPanelInsertar().getTxtCantidad().getText().trim();
        String precioStr = mainFrame.getPanelInsertar().getTxtPrecio().getText().trim();
        Categoria categoriaSeleccionada = (Categoria) mainFrame.getPanelInsertar().getComboCategoria().getSelectedItem();
        Marca marcaSeleccionada = (Marca) mainFrame.getPanelInsertar().getComboMarca().getSelectedItem();

        // Validaciones básicas de entrada
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "El nombre del producto no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cantidadStr.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Cantidad y Precio son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (categoriaSeleccionada == null || marcaSeleccionada == null) {
            JOptionPane.showMessageDialog(mainFrame, "Debe seleccionar una Categoría y una Marca.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(mainFrame, "La cantidad debe ser un número entero positivo.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (precio <= 0) {
                JOptionPane.showMessageDialog(mainFrame, "El precio debe ser un número positivo.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Crea un objeto Producto con los datos de la UI y los objetos Categoria/Marca
            Producto nuevoProducto = new Producto(nombre, precio, categoriaSeleccionada, marcaSeleccionada);

            // Llama al DAO para insertar el producto y su lote
            productoDAO.insertarProducto(nuevoProducto, cantidad);

            JOptionPane.showMessageDialog(mainFrame, "Producto guardado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.getPanelInsertar().limpiarCampos(); // Limpia la UI después de guardar
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Cantidad y Precio deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error de base de datos al guardar el producto: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Lógica para buscar productos. Obtiene el filtro de la UI y llama al DAO para actualizar la tabla.
     */
    private void buscarProductos() {
        String filtro = mainFrame.getPanelMostrar().getTxtBusqueda().getText().trim();
        try {
            List<Producto> productos = productoDAO.buscarProductos(filtro);
            mainFrame.getPanelMostrar().actualizarTabla(productos); // Actualiza la tabla en la UI
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error al buscar productos: " + e.getMessage(), "Error de Datos", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Lógica para editar un producto seleccionado en la tabla.
     * Abre un diálogo de edición con los datos del producto cargados.
     */
    private void editarProductoSeleccionado() {
        int selectedRow = mainFrame.getPanelMostrar().getTablaProductos().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Seleccione un producto de la tabla para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtiene el ID del producto de la fila seleccionada en la tabla
        int modelRow = mainFrame.getPanelMostrar().getTablaProductos().convertRowIndexToModel(selectedRow);
        int idProducto = (int) mainFrame.getPanelMostrar().getModeloTabla().getValueAt(modelRow, 0);

        try {
            // Obtiene el objeto Producto completo desde la base de datos
            Producto productoAEditar = productoDAO.obtenerProductoPorId(idProducto);
            if (productoAEditar == null) {
                JOptionPane.showMessageDialog(mainFrame, "Producto no encontrado en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Abre el diálogo de edición
            abrirDialogoEditarProducto(productoAEditar);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error al obtener producto para editar: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Crea y muestra el diálogo para editar un producto.
     * @param producto El producto a editar, con sus datos actuales.
     */
    private void abrirDialogoEditarProducto(Producto producto) {
        JDialog dialog = new JDialog(mainFrame, "Editar Producto: " + producto.getNombre(), true); // true para modal
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setLayout(new GridLayout(6, 2, 10, 10)); // Más espacio para los campos

        // CORRECCIÓN APLICADA AQUÍ: Se hace un cast a JPanel antes de aplicar el borde.
        ((JPanel) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtNombre = new JTextField(producto.getNombre());
        JTextField txtPrecio = new JTextField(String.valueOf(producto.getPrecioVenta()));
        JComboBox<Categoria> comboCategoria = new JComboBox<>();
        JComboBox<Marca> comboMarca = new JComboBox<>();

        try {
            // Cargar categorías y marcas en los combobox del diálogo
            List<Categoria> categorias = categoriaDAO.obtenerTodasCategorias();
            for (Categoria cat : categorias) { comboCategoria.addItem(cat); }

            List<Marca> marcas = marcaDAO.obtenerTodasMarcas();
            for (Marca mar : marcas) { comboMarca.addItem(mar); }

            // Seleccionar la categoría y marca actuales del producto
            if (producto.getCategoria() != null) comboCategoria.setSelectedItem(producto.getCategoria());
            if (producto.getMarca() != null) comboMarca.setSelectedItem(producto.getMarca());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dialog, "Error al cargar categorías/marcas para edición: " + e.getMessage(), "Error de Datos", JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            return;
        }

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.addActionListener(e -> {
            try {
                String nuevoNombre = txtNombre.getText().trim();
                double nuevoPrecio = Double.parseDouble(txtPrecio.getText().trim());
                Categoria nuevaCategoria = (Categoria) comboCategoria.getSelectedItem();
                Marca nuevaMarca = (Marca) comboMarca.getSelectedItem();

                // Validaciones en el diálogo de edición
                if (nuevoNombre.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "El nombre no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (nuevoPrecio <= 0) {
                    JOptionPane.showMessageDialog(dialog, "El precio debe ser un número positivo.", "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (nuevaCategoria == null || nuevaMarca == null) {
                    JOptionPane.showMessageDialog(dialog, "Debe seleccionar una Categoría y una Marca.", "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Actualiza el objeto producto con los nuevos datos
                producto.setNombre(nuevoNombre);
                producto.setPrecioVenta(nuevoPrecio);
                producto.setCategoria(nuevaCategoria);
                producto.setMarca(nuevaMarca);

                // Llama al DAO para actualizar el producto en la BD
                productoDAO.actualizarProducto(producto);
                JOptionPane.showMessageDialog(dialog, "Producto actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                buscarProductos(); // Recarga la tabla de productos en la vista principal
                dialog.dispose(); // Cierra el diálogo
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "El precio debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error de base de datos al actualizar el producto: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose()); // Cierra el diálogo sin guardar

        // Añadir componentes al contentPane del diálogo
        dialog.getContentPane().add(new JLabel("Nombre:"));
        dialog.getContentPane().add(txtNombre);
        dialog.getContentPane().add(new JLabel("Precio Venta:"));
        dialog.getContentPane().add(txtPrecio);
        dialog.getContentPane().add(new JLabel("Categoría:"));
        dialog.getContentPane().add(comboCategoria);
        dialog.getContentPane().add(new JLabel("Marca:"));
        dialog.getContentPane().add(comboMarca);
        dialog.getContentPane().add(btnCancelar);
        dialog.getContentPane().add(btnGuardar);

        dialog.setVisible(true);
    }

    /**
     * Lógica para eliminar un producto seleccionado en la tabla.
     * Pide confirmación al usuario antes de eliminar.
     */
    private void eliminarProductoSeleccionado() {
        int selectedRow = mainFrame.getPanelMostrar().getTablaProductos().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Seleccione un producto de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = mainFrame.getPanelMostrar().getTablaProductos().convertRowIndexToModel(selectedRow);
        int idProducto = (int) mainFrame.getPanelMostrar().getModeloTabla().getValueAt(modelRow, 0);
        String nombreProducto = (String) mainFrame.getPanelMostrar().getModeloTabla().getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(mainFrame,
                "¿Está seguro de que desea eliminar el producto '" + nombreProducto + "'?\n" +
                        "¡Esta acción también eliminará todos los lotes asociados y es irreversible!",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                productoDAO.eliminarProducto(idProducto);
                JOptionPane.showMessageDialog(mainFrame, "Producto eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                buscarProductos(); // Recarga la tabla después de la eliminación
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainFrame, "Error al eliminar el producto: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Método principal para iniciar la aplicación.
     * Crea una instancia de AppMain que se encarga de todo.
     */
    public static void main(String[] args) {
        // Ejecuta la aplicación en el Event Dispatch Thread (EDT) de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Intenta conectar a la BD al inicio
                ConexionBD.getConnection();
                new AppMain(); // Crea una instancia de AppMain, que a su vez inicia la UI y la lógica.
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al iniciar la aplicación. Fallo de conexión a la base de datos: " + e.getMessage(),
                        "Error de Inicio", JOptionPane.ERROR_MESSAGE);
                System.exit(1); // Salir si no se puede conectar
            }
        });
    }
}