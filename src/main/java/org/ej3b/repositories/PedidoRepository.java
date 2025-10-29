package org.ej3b.repositories;

import org.ej3b.config.DatabaseConfig;
import org.ej3b.models.Pedido;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {
    
    public List<Pedido> findAll() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM Pedido";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                pedidos.add(mapResultSetToPedido(rs));
            }
        }
        return pedidos;
    }
    
    public Pedido findById(int id) throws SQLException {
        String sql = "SELECT * FROM Pedido WHERE ID_Pedido = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPedido(rs);
                }
            }
        }
        return null;
    }
    
    public List<Pedido> findByUsuario(int idUsuario) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM Pedido WHERE ID_Usuario = ? ORDER BY Fecha DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapResultSetToPedido(rs));
                }
            }
        }
        return pedidos;
    }
    
    public List<Pedido> findByNumeroPedido(String numeroPedido) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM Pedido WHERE Numero_Pedido = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroPedido);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapResultSetToPedido(rs));
                }
            }
        }
        return pedidos;
    }
    
    public List<Pedido> findByEstado(String estado) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM Pedido WHERE Estado = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapResultSetToPedido(rs));
                }
            }
        }
        return pedidos;
    }
    
    public int create(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO Pedido (Numero_Pedido, ID_Usuario, ID_Producto, Cantidad, ID_Metodo_Pago, ID_Direccion, Total, Estado, Fecha) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, pedido.getNumeroPedido());
            stmt.setInt(2, pedido.getIdUsuario());
            stmt.setInt(3, pedido.getIdProducto());
            stmt.setInt(4, pedido.getCantidad());
            stmt.setInt(5, pedido.getIdMetodoPago());
            stmt.setInt(6, pedido.getIdDireccion());
            stmt.setDouble(7, pedido.getTotal());
            stmt.setString(8, pedido.getEstado());
            stmt.setTimestamp(9, Timestamp.valueOf(pedido.getFecha()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }
    
    public boolean update(Pedido pedido) throws SQLException {
        String sql = "UPDATE Pedido SET Numero_Pedido = ?, ID_Usuario = ?, ID_Producto = ?, Cantidad = ?, ID_Metodo_Pago = ?, ID_Direccion = ?, Total = ?, Estado = ?, Fecha = ? WHERE ID_Pedido = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, pedido.getNumeroPedido());
            stmt.setInt(2, pedido.getIdUsuario());
            stmt.setInt(3, pedido.getIdProducto());
            stmt.setInt(4, pedido.getCantidad());
            stmt.setInt(5, pedido.getIdMetodoPago());
            stmt.setInt(6, pedido.getIdDireccion());
            stmt.setDouble(7, pedido.getTotal());
            stmt.setString(8, pedido.getEstado());
            stmt.setTimestamp(9, Timestamp.valueOf(pedido.getFecha()));
            stmt.setInt(10, pedido.getIdPedido());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateEstado(int idPedido, String nuevoEstado) throws SQLException {
        String sql = "UPDATE Pedido SET Estado = ? WHERE ID_Pedido = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idPedido);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Pedido WHERE ID_Pedido = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    private Pedido mapResultSetToPedido(ResultSet rs) throws SQLException {
        Timestamp fechaTimestamp = rs.getTimestamp("Fecha");
        LocalDateTime fecha = fechaTimestamp != null ? fechaTimestamp.toLocalDateTime() : null;
        
        return new Pedido(
            rs.getInt("ID_Pedido"),
            rs.getString("Numero_Pedido"),
            rs.getInt("ID_Usuario"),
            rs.getInt("ID_Producto"),
            rs.getInt("Cantidad"),
            rs.getInt("ID_Metodo_Pago"),
            rs.getInt("ID_Direccion"),
            rs.getDouble("Total"),
            rs.getString("Estado"),
            fecha
        );
    }
}
