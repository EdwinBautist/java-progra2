package papeleria.model.entity;

public class Categoria {
    private int idCategoria;
    private String nombre;

    public Categoria(int idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    // Constructor para cuando el ID es autoincremental en la BD
    public Categoria(String nombre) {
        this(-1, nombre); // Usamos -1 para indicar que no tiene ID aún
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
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

    // Para comparar objetos Categoria, útil en JComboBox.setSelectedItem()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return idCategoria == categoria.idCategoria;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idCategoria);
    }
}