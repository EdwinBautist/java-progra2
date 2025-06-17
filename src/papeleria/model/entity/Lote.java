package papeleria.model.entity;

import java.sql.Date;

public class Lote {
    private int idLote;
    private Date fechaEntrada;
    private double precioUni;
    private int cantidad;
    private int idProducto; // ID del producto al que pertenece el lote

    // Constructor completo
    public Lote(int idLote, Date fechaEntrada, double precioUni, int cantidad, int idProducto) {
        this.idLote = idLote;
        this.fechaEntrada = fechaEntrada;
        this.precioUni = precioUni;
        this.cantidad = cantidad;
        this.idProducto = idProducto;
    }

    // Constructor para nuevas entradas de lote (sin ID, será autogenerado)
    public Lote(Date fechaEntrada, double precioUni, int cantidad, int idProducto) {
        this(-1, fechaEntrada, precioUni, cantidad, idProducto);
    }

    // --- Getters y Setters ---
    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public double getPrecioUni() {
        return precioUni;
    }

    public void setPrecioUni(double precioUni) {
        this.precioUni = precioUni;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}