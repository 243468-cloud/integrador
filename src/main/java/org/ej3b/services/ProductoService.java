package org.ej3b.services;

import org.ej3b.models.Producto;
import org.ej3b.repositories.ProductoRepository;

import java.sql.SQLException;
import java.util.List;

public class ProductoService {
    private final ProductoRepository productoRepository;
    
    public ProductoService() {
        this.productoRepository = new ProductoRepository();
    }
    
    public List<Producto> getAllProductos() throws SQLException {
        return productoRepository.findAll();
    }
    
    public Producto getProductoById(int id) throws SQLException {
        return productoRepository.findById(id);
    }
    
    public List<Producto> getProductosConStock() throws SQLException {
        return productoRepository.findByStockGreaterThan(0);
    }
    
    public Producto createProducto(Producto producto) throws SQLException {
        int id = productoRepository.create(producto);
        producto.setIdProducto(id);
        return producto;
    }
    
    public boolean updateProducto(Producto producto) throws SQLException {
        return productoRepository.update(producto);
    }
    
    public boolean updateStock(int idProducto, int newStock) throws SQLException {
        return productoRepository.updateStock(idProducto, newStock);
    }
    
    public boolean deleteProducto(int id) throws SQLException {
        return productoRepository.delete(id);
    }
    
    public boolean verificarStock(int idProducto, int cantidadSolicitada) throws SQLException {
        Producto producto = productoRepository.findById(idProducto);
        return producto != null && producto.getStock() >= cantidadSolicitada;
    }
    
    public boolean reducirStock(int idProducto, int cantidad) throws SQLException {
        Producto producto = productoRepository.findById(idProducto);
        if (producto != null && producto.getStock() >= cantidad) {
            int nuevoStock = producto.getStock() - cantidad;
            return productoRepository.updateStock(idProducto, nuevoStock);
        }
        return false;
    }
    
    public double calcularPrecioConDescuento(double precioBase, int cantidad) {
        // Descuento por volumen (mayoreo)
        if (cantidad >= 10) {
            return precioBase * 0.85; // 15% de descuento para mayoreo
        } else if (cantidad >= 5) {
            return precioBase * 0.90; // 10% de descuento para cantidades medianas
        }
        return precioBase; // Sin descuento para menudeo
    }
}
