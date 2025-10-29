package org.ej3b.services;

import org.ej3b.models.*;
import org.ej3b.repositories.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;
    private final CarritoRepository carritoRepository;
    
    public PedidoService() {
        this.pedidoRepository = new PedidoRepository();
        this.productoService = new ProductoService();
        this.carritoRepository = new CarritoRepository();
    }
    
    public List<Pedido> getAllPedidos() throws SQLException {
        return pedidoRepository.findAll();
    }
    
    public Pedido getPedidoById(int id) throws SQLException {
        return pedidoRepository.findById(id);
    }
    
    public List<Pedido> getPedidosByUsuario(int idUsuario) throws SQLException {
        return pedidoRepository.findByUsuario(idUsuario);
    }
    
    public List<Pedido> getPedidosByNumero(String numeroPedido) throws SQLException {
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }
    
    public List<Pedido> getPedidosByEstado(String estado) throws SQLException {
        return pedidoRepository.findByEstado(estado);
    }
    
    public Pedido createPedido(Pedido pedido) throws SQLException {
        // Verificar stock antes de crear el pedido
        if (!productoService.verificarStock(pedido.getIdProducto(), pedido.getCantidad())) {
            throw new SQLException("Stock insuficiente para el producto solicitado");
        }
        
        // Generar número de pedido único si no se proporciona
        if (pedido.getNumeroPedido() == null || pedido.getNumeroPedido().isEmpty()) {
            pedido.setNumeroPedido("PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        
        // Establecer fecha actual si no se proporciona
        if (pedido.getFecha() == null) {
            pedido.setFecha(LocalDateTime.now());
        }
        
        // Establecer estado inicial
        if (pedido.getEstado() == null || pedido.getEstado().isEmpty()) {
            pedido.setEstado("Pendiente");
        }
        
        int id = pedidoRepository.create(pedido);
        pedido.setIdPedido(id);
        
        // Reducir stock después de crear el pedido
        productoService.reducirStock(pedido.getIdProducto(), pedido.getCantidad());
        
        return pedido;
    }
    
    public boolean updatePedido(Pedido pedido) throws SQLException {
        return pedidoRepository.update(pedido);
    }
    
    public boolean updateEstadoPedido(int idPedido, String nuevoEstado) throws SQLException {
        return pedidoRepository.updateEstado(idPedido, nuevoEstado);
    }
    
    public boolean deletePedido(int id) throws SQLException {
        return pedidoRepository.delete(id);
    }
    
    public Pedido procesarCarrito(int idUsuario, int idMetodoPago, int idDireccion) throws SQLException {
        List<Carrito> itemsCarrito = carritoRepository.findByUsuario(idUsuario);
        
        if (itemsCarrito.isEmpty()) {
            throw new SQLException("El carrito está vacío");
        }
        
        String numeroPedido = "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Pedido pedidoPrincipal = null;
        
        for (Carrito item : itemsCarrito) {
            // Verificar stock para cada item
            if (!productoService.verificarStock(item.getIdProducto(), item.getCantidad())) {
                throw new SQLException("Stock insuficiente para el producto ID: " + item.getIdProducto());
            }
            
            // Calcular precio con descuentos
            Producto producto = productoService.getProductoById(item.getIdProducto());
            double precioConDescuento = productoService.calcularPrecioConDescuento(producto.getPrecio(), item.getCantidad());
            double totalItem = precioConDescuento * item.getCantidad();
            
            Pedido pedido = new Pedido();
            pedido.setNumeroPedido(numeroPedido);
            pedido.setIdUsuario(idUsuario);
            pedido.setIdProducto(item.getIdProducto());
            pedido.setCantidad(item.getCantidad());
            pedido.setIdMetodoPago(idMetodoPago);
            pedido.setIdDireccion(idDireccion);
            pedido.setTotal(totalItem);
            pedido.setEstado("Pendiente");
            pedido.setFecha(LocalDateTime.now());
            
            int id = pedidoRepository.create(pedido);
            pedido.setIdPedido(id);
            
            // Reducir stock
            productoService.reducirStock(item.getIdProducto(), item.getCantidad());
            
            if (pedidoPrincipal == null) {
                pedidoPrincipal = pedido;
            }
        }
        
        // Limpiar carrito después de procesar
        carritoRepository.deleteByUsuario(idUsuario);
        
        return pedidoPrincipal;
    }
    
    public double calcularTotalPedido(String numeroPedido) throws SQLException {
        List<Pedido> pedidos = pedidoRepository.findByNumeroPedido(numeroPedido);
        return pedidos.stream().mapToDouble(Pedido::getTotal).sum();
    }
}
