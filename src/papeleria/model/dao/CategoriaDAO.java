package papeleria.model.dao;

import papeleria.model.entity.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    /**
     * Inserta una nueva categoría en la base de datos.
     * @param categoria El objeto Categoria a insertar.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public void insertarCategoria(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO Categoria (nombre) VALUES (?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, categoria.getNombre());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                categoria.setId(rs.getInt(1)); // Asigna el ID generado a la entidad
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            ConexionBD.closeConnection();
        }
    }

    /**
     * Obtiene todas las categorías de la base de datos.
     * @return Una lista de objetos Categoria.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public List<Categoria> obtenerTodasCategorias() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre FROM Categoria ORDER BY nombre";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
                );
                categorias.add(categoria);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            ConexionBD.closeConnection();
        }
        return categorias;
    }

    /**
     * Actualiza una categoría existente.
     * @param categoria El objeto Categoria con los datos actualizados.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public void actualizarCategoria(Categoria categoria) throws SQLException {
        String sql = "UPDATE Categoria SET nombre = ? WHERE id_categoria = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexionBD.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, categoria.getNombre());
            pstmt.setInt(2, categoria.getId());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
            ConexionBD.closeConnection();
        }
    }

    /**
     * Elimina una categoría por su ID.
     * @param idCategoria El ID de la categoría a eliminar.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public void eliminarCategoria(int idCategoria) throws SQLException {
        String sql = "DELETE FROM Categoria WHERE id_categoria = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexionBD.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idCategoria);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
            ConexionBD.closeConnection();
        }
    }
}