package papeleria.model.dao;

import papeleria.model.entity.Cliente;
import papeleria.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public List<Cliente> obtenerTodosClientes() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                lista.add(c);
            }
        }
        return lista;
    }

    public boolean insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (nombre, telefono, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.setString(3, cliente.getEmail());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET nombre = ?, telefono = ?, email = ? WHERE id_cliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.setString(3, cliente.getEmail());
            pstmt.setInt(4, cliente.getId());

            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idCliente) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE id_cliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            return pstmt.executeUpdate() > 0;
        }
    }

    public Cliente obtenerPorId(int idCliente) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE id_cliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );
                }
            }
        }
        return null;
    }
}