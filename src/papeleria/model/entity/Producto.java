package papeleria.model.entity;

public class Producto {
    private int id;
    private String nombre;
    private double precioVenta;
    private int stock;
    private int idCategoria;
    private int idMarca;

    public Producto() {}

    public Producto(int id, String nombre, double precioVenta, int stock, int idCategoria, int idMarca) {
        this.id = id;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.idCategoria = idCategoria;
        this.idMarca = idMarca;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public int getIdMarca() { return idMarca; }
    public void setIdMarca(int idMarca) { this.idMarca = idMarca; }

    @Override
    public String toString() {
        return nombre + " (Stock: " + stock + ")";
    }
}