package papeleria.model.entity;

import java.util.Date;
import java.util.List;

/**
 * Clase que representa una venta en el sistema.
 */
public class Venta {
    private int id;
    private Date fecha;
    private double precioTotal;
    private int idCliente;
    private int idEmpleado;
    private List<DetalleVenta> detalles;
    private String nombreCliente; // Campo adicional para visualización
    private String nombreEmpleado; // Campo adicional para visualización

    public Venta() {}

    public Venta(int id, Date fecha, double precioTotal, int idCliente, int idEmpleado) {
        this.id = id;
        this.fecha = fecha;
        this.precioTotal = precioTotal;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        if (precioTotal < 0) {
            throw new IllegalArgumentException("El precio total no puede ser negativo");
        }
        this.precioTotal = precioTotal;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    // Métodos utilitarios
    public String getResumen() {
        return String.format("Venta #%d - %s - Total: $%,.2f",
                id,
                fecha.toString(),
                precioTotal);
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", precioTotal=" + precioTotal +
                ", idCliente=" + idCliente +
                ", idEmpleado=" + idEmpleado +
                '}';
    }
}