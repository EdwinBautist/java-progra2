package papeleria.model.entity;

public class Producto {
    private int id;
    private String nombre;
    private double precioVenta;
    private Categoria categoria;
    private Marca marca;
    private int stock; // Nuevo campo para el stock

    // Constructor completo con todos los campos, incluyendo ID y stock
    public Producto(int id, String nombre, double precioVenta, Categoria categoria, Marca marca, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.categoria = categoria;
        this.marca = marca;
        this.stock = stock;
    }

    public Producto() {
        // Constructor vacío para frameworks y DAOs
    }

    // Constructor para insertar un nuevo producto (sin ID, ya que la BD lo genera)
    // El stock inicial se pasará al DAO y se manejará en Lote y en el campo stock_actual del Producto
    public Producto(String nombre, double precioVenta, Categoria categoria, Marca marca) {
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.categoria = categoria;
        this.marca = marca;
        this.stock = 0; // Se inicializa a 0, el DAO lo actualizará
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioVenta() {
        return precioForDisplay();
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    // Getter y Setter para stock
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Método para formato de precio
    private double precioForDisplay() {
        return Math.round(this.precioVenta * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return nombre; // Útil para JComboBox
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id == producto.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}