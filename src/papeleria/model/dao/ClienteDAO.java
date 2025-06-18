package papeleria.model.dao;

import papeleria.model.entity.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import papeleria.utils.DatabaseConnection;

public class ClienteDAO {
    private static final String SQL_INSERT = "INSERT INTO Cliente (nombre, telefono, email) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT id_cliente, nombre, telefono, email FROM Cliente ORDER BY nombre";
    private static final String SQL_SELECT_BY_ID = "SELECT id_cliente, nombre, telefono, email FROM Cliente WHERE id_cliente = ?";
    private static final String SQL_UPDATE = "UPDATE Cliente SET nombre = ?, telefono = ?, email = ? WHERE id_cliente = ?";
    private static final String SQL_DELETE = "DELETE FROM Cliente WHERE id_cliente = ?";
    private static final String SQL_SEARCH = "SELECT id_cliente, nombre, telefono, email FROM Cliente WHERE nombre LIKE ? OR telefono LIKE ? OR email LIKE ? ORDER BY nombre";

    public void crearCliente(Cliente cliente) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.setString(3, cliente.getEmail());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Cliente> obtenerTodosClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    public Cliente obtenerClientePorId(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            pstmt.setInt(1, id);
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

    public void actualizarCliente(Cliente cliente) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {

            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.setString(3, cliente.getEmail());
            pstmt.setInt(4, cliente.getId());
            pstmt.executeUpdate();
        }
    }

    public void eliminarCliente(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Cliente> buscarClientes(String criterio) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String parametro = "%" + criterio + "%";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SEARCH)) {

            pstmt.setString(1, parametro);
            pstmt.setString(2, parametro);
            pstmt.setString(3, parametro);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );
                    clientes.add(cliente);
                }
            }
        }
        return clientes;
    }

    public boolean existeClienteConEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Cliente WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}