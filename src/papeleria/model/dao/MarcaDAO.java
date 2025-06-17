package papeleria.model.dao;

import papeleria.model.entity.Marca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAO {

    /**
     * Inserta una nueva marca en la base de datos.
     * @param marca La marca a insertar. El ID se generará automáticamente en la BD.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public void insertarMarca(Marca marca) throws SQLException {
        String sql = "INSERT INTO Marca (nombre) VALUES (?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, marca.getNombre());
            stmt.executeUpdate();

            // Obtener el ID generado automáticamente
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    marca.setId(rs.getInt(1)); // Asignar el ID a la entidad Marca
                }
            }
        }
    }

    /**
     * Actualiza una marca existente en la base de datos.
     * @param marca La marca con los datos actualizados.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public void actualizarMarca(Marca marca) throws SQLException {
        String sql = "UPDATE Marca SET nombre = ? WHERE id_marca = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, marca.getNombre());
            stmt.setInt(2, marca.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina una marca de la base de datos por su ID.
     * @param idMarca El ID de la marca a eliminar.
     * @throws SQLException Si ocurre un error de acceso a la base de datos (ej. si hay productos asociados).
     */
    public void eliminarMarca(int idMarca) throws SQLException {
        String sql = "DELETE FROM Marca WHERE id_marca = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMarca);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se encontró la marca con ID: " + idMarca + " para eliminar.");
            }
        }
    }

    /**
     * Obtiene una marca por su ID.
     * @param idMarca El ID de la marca a obtener.
     * @return El objeto Marca si se encuentra, null en caso contrario.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public Marca obtenerMarcaPorId(int idMarca) throws SQLException {
        String sql = "SELECT id_marca, nombre FROM Marca WHERE id_marca = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMarca);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Marca(rs.getInt("id_marca"), rs.getString("nombre"));
                }
            }
        }
        return null;
    }

    /**
     * Obtiene todas las marcas de la base de datos.
     * @return Una lista de objetos Marca.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public List<Marca> obtenerTodasMarcas() throws SQLException {
        List<Marca> marcas = new ArrayList<>();
        String sql = "SELECT id_marca, nombre FROM Marca";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                marcas.add(new Marca(rs.getInt("id_marca"), rs.getString("nombre")));
            }
        }
        return marcas;
    }

    /**
     * Obtiene una marca por su nombre (útil para verificar duplicados, si es necesario).
     * @param nombre El nombre de la marca a buscar.
     * @return El objeto Marca si se encuentra, null en caso contrario.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public Marca obtenerMarcaPorNombre(String nombre) throws SQLException {
        String sql = "SELECT id_marca, nombre FROM Marca WHERE nombre = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Marca(rs.getInt("id_marca"), rs.getString("nombre"));
                }
            }
        }
        return null;
    }
}