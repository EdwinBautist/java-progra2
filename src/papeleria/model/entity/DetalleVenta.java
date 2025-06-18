package papeleria.model.entity;

import java.util.Objects;

/**
 * Clase que representa un detalle de venta en el sistema.
 */
public class DetalleVenta {
    private int idVenta;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;
    private String nombreProducto; // Campo adicional para mostrar información

    public DetalleVenta() {}

    public DetalleVenta(int idVenta, int idProducto, int cantidad, double precioUnitario) {
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // Getters y Setters
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        if (precioUnitario <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }
        this.precioUnitario = precioUnitario;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    // Método para calcular el subtotal
    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleVenta that = (DetalleVenta) o;
        return idVenta == that.idVenta &&
                idProducto == that.idProducto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVenta, idProducto);
    }

    @Override
    public String toString() {
        return "DetalleVenta{" +
                "idVenta=" + idVenta +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                '}';
    }

    // Método para crear un resumen del detalle
    public String getResumen() {
        return String.format("%d x %s - $%,.2f c/u (Subtotal: $%,.2f)",
                cantidad,
                nombreProducto != null ? nombreProducto : "Producto " + idProducto,
                precioUnitario,
                getSubtotal());
    }
}