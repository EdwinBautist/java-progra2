package papeleria.model.entity;

public class Marca {
    private int idMarca;
    private String nombre;

    public Marca(int idMarca, String nombre) {
        this.idMarca = idMarca;
        this.nombre = nombre;
    }

    // Constructor para cuando el ID es autoincremental en la BD
    public Marca(String nombre) {
        this(-1, nombre);
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre; // Importante para que el JComboBox muestre el nombre
    }

    // Para comparar objetos Marca, útil en JComboBox.setSelectedItem()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marca marca = (Marca) o;
        return idMarca == marca.idMarca;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idMarca);
    }
}