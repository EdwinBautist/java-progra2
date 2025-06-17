package papeleria.model.entity;

import java.sql.Timestamp;

public class Producto {
    private int idProducto;
    private String nombre;
    private double precioVenta;
    private Categoria categoria; // Objeto Categoria asociado
    private Marca marca;         // Objeto Marca asociado
    private int stockTotal;      // Stock actual (calculado de lotes)
    private Timestamp ultimaFechaRegistro; // Fecha del último lote o registro

    // Constructor completo para productos existentes (con categoria y marca completas)
    public Producto(int idProducto, String nombre, double precioVenta, Categoria categoria, Marca marca) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.categoria = categoria;
        this.marca = marca;
    }

    // Constructor para crear nuevos productos (sin ID, será autogenerado)
    public Producto(String nombre, double precioVenta, Categoria categoria, Marca marca) {
        this(-1, nombre, precioVenta, categoria, marca);
    }

    // Constructor para la vista de listado de productos (incluye stock y fecha)
    public Producto(int idProducto, String nombre, double precioVenta, int stockTotal, Timestamp ultimaFechaRegistro) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.stockTotal = stockTotal;
        this.ultimaFechaRegistro = ultimaFechaRegistro;
    }

    // --- Getters y Setters ---
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioVenta() {
        return precioVenta;
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

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }

    public Timestamp getUltimaFechaRegistro() {
        return ultimaFechaRegistro;
    }

    public void setUltimaFechaRegistro(Timestamp ultimaFechaRegistro) {
        this.ultimaFechaRegistro = ultimaFechaRegistro;
    }
}