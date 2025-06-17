package main.java.models;
public class Usuario {
    private int idEmpleado;
    private String email;
    private String contrasena; // Idealmente, esto debería ser un hash (no texto plano)
    private String nombre;
    private String cargo;

    // Constructor
    public Usuario(int idEmpleado, String email, String contrasena, String nombre, String cargo) {
        this.idEmpleado = idEmpleado;
        this.email = email;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.cargo = cargo;
    }

    // Getters
    public int getIdEmpleado() { return idEmpleado; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public String getCargo() { return cargo; }
    // No exponer getContrasena() por seguridad
}