package org.ej3b.repositories;

import org.ej3b.config.DatabaseConfig;
import org.ej3b.models.Carrito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarritoRepository {
    
    public List<Carrito> findAll() throws SQLException {
        List<Carrito> carritos = new ArrayList<>();
        String sql = "SELECT * FROM Carrito";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                carritos.add(mapResultSetToCarrito(rs));
            }
        }
        return carritos;
    }
    
    public List<Carrito> findByUsuario(int idUsuario) throws SQLException {
        List<Carrito> carritos = new ArrayList<>();
        String sql = "SELECT * FROM Carrito WHERE ID_Usuario = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    carritos.add(mapResultSetToCarrito(rs));
                }
            }
        }
        return carritos;
    }
    
    public Carrito findByUsuarioAndProducto(int idUsuario, int idProducto) throws SQLException {
        String sql = "SELECT * FROM Carrito WHERE ID_Usuario = ? AND ID_Producto = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idProducto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCarrito(rs);
                }
            }
        }
        return null;
    }
    
    public int create(Carrito carrito) throws SQLException {
        String sql = "INSERT INTO Carrito (ID_Usuario, ID_Producto, Cantidad) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, carrito.getIdUsuario());
            stmt.setInt(2, carrito.getIdProducto());
            stmt.setInt(3, carrito.getCantidad());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating cart item failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating cart item failed, no ID obtained.");
                }
            }
        }
    }
    
    public boolean update(Carrito carrito) throws SQLException {
        String sql = "UPDATE Carrito SET Cantidad = ? WHERE ID_Carrito = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, carrito.getCantidad());
            stmt.setInt(2, carrito.getIdCarrito());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateCantidad(int idCarrito, int nuevaCantidad) throws SQLException {
        String sql = "UPDATE Carrito SET Cantidad = ? WHERE ID_Carrito = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nuevaCantidad);
            stmt.setInt(2, idCarrito);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Carrito WHERE ID_Carrito = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteByUsuario(int idUsuario) throws SQLException {
        String sql = "DELETE FROM Carrito WHERE ID_Usuario = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteByUsuarioAndProducto(int idUsuario, int idProducto) throws SQLException {
        String sql = "DELETE FROM Carrito WHERE ID_Usuario = ? AND ID_Producto = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idProducto);
            return stmt.executeUpdate() > 0;
        }
    }
    
    private Carrito mapResultSetToCarrito(ResultSet rs) throws SQLException {
        return new Carrito(
            rs.getInt("ID_Carrito"),
            rs.getInt("ID_Usuario"),
            rs.getInt("ID_Producto"),
            rs.getInt("Cantidad")
        );
    }
}
