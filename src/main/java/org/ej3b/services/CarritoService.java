package org.ej3b.services;

import org.ej3b.models.Carrito;
import org.ej3b.models.Producto;
import org.ej3b.repositories.CarritoRepository;

import java.sql.SQLException;
import java.util.List;

public class CarritoService {
    private final CarritoRepository carritoRepository;
    private final ProductoService productoService;
    
    public CarritoService() {
        this.carritoRepository = new CarritoRepository();
        this.productoService = new ProductoService();
    }
    
    public List<Carrito> getCarritoByUsuario(int idUsuario) throws SQLException {
        return carritoRepository.findByUsuario(idUsuario);
    }
    
    public Carrito addToCarrito(int idUsuario, int idProducto, int cantidad) throws SQLException {
        // Verificar que el producto existe y tiene stock
        Producto producto = productoService.getProductoById(idProducto);
        if (producto == null) {
            throw new SQLException("Producto no encontrado");
        }
        
        if (!productoService.verificarStock(idProducto, cantidad)) {
            throw new SQLException("Stock insuficiente");
        }
        
        // Verificar si el producto ya está en el carrito
        Carrito existingItem = carritoRepository.findByUsuarioAndProducto(idUsuario, idProducto);
        
        if (existingItem != null) {
            // Actualizar cantidad
            int nuevaCantidad = existingItem.getCantidad() + cantidad;
            if (!productoService.verificarStock(idProducto, nuevaCantidad)) {
                throw new SQLException("Stock insuficiente para la cantidad solicitada");
            }
            carritoRepository.updateCantidad(existingItem.getIdCarrito(), nuevaCantidad);
            existingItem.setCantidad(nuevaCantidad);
            return existingItem;
        } else {
            // Crear nuevo item en el carrito
            Carrito nuevoItem = new Carrito();
            nuevoItem.setIdUsuario(idUsuario);
            nuevoItem.setIdProducto(idProducto);
            nuevoItem.setCantidad(cantidad);
            
            int id = carritoRepository.create(nuevoItem);
            nuevoItem.setIdCarrito(id);
            return nuevoItem;
        }
    }
    
    public boolean updateCantidadCarrito(int idCarrito, int nuevaCantidad) throws SQLException {
        Carrito item = carritoRepository.findAll().stream()
            .filter(c -> c.getIdCarrito() == idCarrito)
            .findFirst()
            .orElse(null);
            
        if (item == null) {
            throw new SQLException("Item del carrito no encontrado");
        }
        
        if (!productoService.verificarStock(item.getIdProducto(), nuevaCantidad)) {
            throw new SQLException("Stock insuficiente");
        }
        
        return carritoRepository.updateCantidad(idCarrito, nuevaCantidad);
    }
    
    public boolean removeFromCarrito(int idCarrito) throws SQLException {
        return carritoRepository.delete(idCarrito);
    }
    
    public boolean removeFromCarritoByProducto(int idUsuario, int idProducto) throws SQLException {
        return carritoRepository.deleteByUsuarioAndProducto(idUsuario, idProducto);
    }
    
    public boolean clearCarrito(int idUsuario) throws SQLException {
        return carritoRepository.deleteByUsuario(idUsuario);
    }
    
    public double calcularTotalCarrito(int idUsuario) throws SQLException {
        List<Carrito> items = carritoRepository.findByUsuario(idUsuario);
        double total = 0.0;
        
        for (Carrito item : items) {
            Producto producto = productoService.getProductoById(item.getIdProducto());
            if (producto != null) {
                double precioConDescuento = productoService.calcularPrecioConDescuento(producto.getPrecio(), item.getCantidad());
                total += precioConDescuento * item.getCantidad();
            }
        }
        
        return total;
    }
    
    public int contarItemsCarrito(int idUsuario) throws SQLException {
        return carritoRepository.findByUsuario(idUsuario).size();
    }
}
