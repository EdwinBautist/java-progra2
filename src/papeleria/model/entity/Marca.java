package papeleria.model.entity;

/**
 * Representa una marca de productos en la papelería.
 */
public class Marca {
    private int id; // Campo para el ID
    private String nombre;

    // Constructor con ID (usado al recuperar de la BD)
    public Marca(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Constructor sin ID (usado al insertar un nuevo objeto, el ID será generado por la BD)
    public Marca(String nombre) {
        this.nombre = nombre;
    }

    // --- Getters y Setters ---

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

    /**
     * Sobreescribe el método toString() para que el JComboBox muestre el nombre de la marca.
     * @return El nombre de la marca.
     */
    @Override
    public String toString() {
        return nombre;
    }

    /**
     * Sobreescribe equals y hashCode para que JComboBox.setSelectedItem() funcione correctamente
     * cuando se usan objetos Marca. Dos marcas son iguales si tienen el mismo ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marca marca = (Marca) o;
        return id == marca.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}