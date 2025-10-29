package org.ej3b.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.ej3b.models.Pedido;
import org.ej3b.services.PedidoService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoController {
    private final PedidoService pedidoService;
    
    public PedidoController() {
        this.pedidoService = new PedidoService();
    }
    
    public void getAllPedidos(Context ctx) {
        try {
            List<Pedido> pedidos = pedidoService.getAllPedidos();
            ctx.json(pedidos);
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al obtener pedidos: " + e.getMessage()));
        }
    }
    
    public void getPedidoById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Pedido pedido = pedidoService.getPedidoById(id);
            
            if (pedido != null) {
                ctx.json(pedido);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Pedido no encontrado"));
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de pedido inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al obtener pedido: " + e.getMessage()));
        }
    }
    
    public void getPedidosByUsuario(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("idUsuario"));
            List<Pedido> pedidos = pedidoService.getPedidosByUsuario(idUsuario);
            ctx.json(pedidos);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de usuario inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al obtener pedidos del usuario: " + e.getMessage()));
        }
    }
    
    public void getPedidosByNumero(Context ctx) {
        try {
            String numeroPedido = ctx.pathParam("numeroPedido");
            List<Pedido> pedidos = pedidoService.getPedidosByNumero(numeroPedido);
            ctx.json(pedidos);
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al obtener pedidos por número: " + e.getMessage()));
        }
    }
    
    public void getPedidosByEstado(Context ctx) {
        try {
            String estado = ctx.pathParam("estado");
            List<Pedido> pedidos = pedidoService.getPedidosByEstado(estado);
            ctx.json(pedidos);
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al obtener pedidos por estado: " + e.getMessage()));
        }
    }
    
    public void createPedido(Context ctx) {
        try {
            Pedido pedido = ctx.bodyAsClass(Pedido.class);
            
            // Validaciones básicas
            if (pedido.getIdUsuario() == 0) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El ID de usuario es requerido"));
                return;
            }
            
            if (pedido.getIdProducto() == 0) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El ID de producto es requerido"));
                return;
            }
            
            if (pedido.getCantidad() <= 0) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("La cantidad debe ser mayor a 0"));
                return;
            }
            
            if (pedido.getIdMetodoPago() == 0) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El método de pago es requerido"));
                return;
            }
            
            if (pedido.getIdDireccion() == 0) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("La dirección es requerida"));
                return;
            }
            
            Pedido nuevoPedido = pedidoService.createPedido(pedido);
            ctx.status(HttpStatus.CREATED).json(nuevoPedido);
            
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al crear pedido: " + e.getMessage()));
        }
    }
    
    public void procesarCarrito(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("idUsuario"));
            Map<String, Integer> requestData = ctx.bodyAsClass(Map.class);
            
            Integer idMetodoPago = requestData.get("idMetodoPago");
            Integer idDireccion = requestData.get("idDireccion");
            
            if (idMetodoPago == null) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El método de pago es requerido"));
                return;
            }
            
            if (idDireccion == null) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("La dirección es requerida"));
                return;
            }
            
            Pedido pedido = pedidoService.procesarCarrito(idUsuario, idMetodoPago, idDireccion);
            ctx.status(HttpStatus.CREATED).json(pedido);
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de usuario inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al procesar carrito: " + e.getMessage()));
        }
    }
    
    public void updatePedido(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Pedido pedido = ctx.bodyAsClass(Pedido.class);
            pedido.setIdPedido(id);
            
            boolean updated = pedidoService.updatePedido(pedido);
            
            if (updated) {
                Pedido pedidoActualizado = pedidoService.getPedidoById(id);
                ctx.json(pedidoActualizado);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Pedido no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de pedido inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al actualizar pedido: " + e.getMessage()));
        }
    }
    
    public void updateEstadoPedido(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Map<String, String> requestData = ctx.bodyAsClass(Map.class);
            String nuevoEstado = requestData.get("estado");
            
            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El campo 'estado' es requerido"));
                return;
            }
            
            boolean updated = pedidoService.updateEstadoPedido(id, nuevoEstado);
            
            if (updated) {
                Pedido pedido = pedidoService.getPedidoById(id);
                ctx.json(pedido);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Pedido no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de pedido inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al actualizar estado: " + e.getMessage()));
        }
    }
    
    public void calcularTotalPedido(Context ctx) {
        try {
            String numeroPedido = ctx.pathParam("numeroPedido");
            double total = pedidoService.calcularTotalPedido(numeroPedido);
            
            Map<String, Object> response = new HashMap<>();
            response.put("numeroPedido", numeroPedido);
            response.put("total", total);
            
            ctx.json(response);
            
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al calcular total: " + e.getMessage()));
        }
    }
    
    public void deletePedido(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean deleted = pedidoService.deletePedido(id);
            
            if (deleted) {
                ctx.status(HttpStatus.NO_CONTENT);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Pedido no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de pedido inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al eliminar pedido: " + e.getMessage()));
        }
    }
    
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
