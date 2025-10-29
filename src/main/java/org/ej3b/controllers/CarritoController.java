package org.ej3b.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.ej3b.models.Carrito;
import org.ej3b.services.CarritoService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoController {
    private final CarritoService carritoService;
    
    public CarritoController() {
        this.carritoService = new CarritoService();
    }
    
    public void getCarritoByUsuario(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("idUsuario"));
            List<Carrito> carrito = carritoService.getCarritoByUsuario(idUsuario);
            ctx.json(carrito);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de usuario inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al obtener carrito: " + e.getMessage()));
        }
    }
    
    public void addToCarrito(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("idUsuario"));
            Map<String, Object> requestData = ctx.bodyAsClass(Map.class);
            
            Integer idProducto = (Integer) requestData.get("idProducto");
            Integer cantidad = (Integer) requestData.get("cantidad");
            
            if (idProducto == null) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El campo 'idProducto' es requerido"));
                return;
            }
            
            if (cantidad == null || cantidad <= 0) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("La cantidad debe ser mayor a 0"));
                return;
            }
            
            Carrito item = carritoService.addToCarrito(idUsuario, idProducto, cantidad);
            ctx.status(HttpStatus.CREATED).json(item);
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de usuario inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al agregar al carrito: " + e.getMessage()));
        }
    }
    
    public void updateCantidadCarrito(Context ctx) {
        try {
            int idCarrito = Integer.parseInt(ctx.pathParam("idCarrito"));
            Map<String, Integer> requestData = ctx.bodyAsClass(Map.class);
            Integer nuevaCantidad = requestData.get("cantidad");
            
            if (nuevaCantidad == null) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El campo 'cantidad' es requerido"));
                return;
            }
            
            if (nuevaCantidad <= 0) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("La cantidad debe ser mayor a 0"));
                return;
            }
            
            boolean updated = carritoService.updateCantidadCarrito(idCarrito, nuevaCantidad);
            
            if (updated) {
                ctx.status(HttpStatus.OK).json(createSuccessResponse("Cantidad actualizada correctamente"));
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Item del carrito no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de carrito inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al actualizar cantidad: " + e.getMessage()));
        }
    }
    
    public void removeFromCarrito(Context ctx) {
        try {
            int idCarrito = Integer.parseInt(ctx.pathParam("idCarrito"));
            boolean removed = carritoService.removeFromCarrito(idCarrito);
            
            if (removed) {
                ctx.status(HttpStatus.NO_CONTENT);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Item del carrito no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de carrito inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al eliminar del carrito: " + e.getMessage()));
        }
    }
    
    public void removeFromCarritoByProducto(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("idUsuario"));
            int idProducto = Integer.parseInt(ctx.pathParam("idProducto"));
            
            boolean removed = carritoService.removeFromCarritoByProducto(idUsuario, idProducto);
            
            if (removed) {
                ctx.status(HttpStatus.NO_CONTENT);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Item del carrito no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al eliminar del carrito: " + e.getMessage()));
        }
    }
    
    public void clearCarrito(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("idUsuario"));
            boolean cleared = carritoService.clearCarrito(idUsuario);
            
            if (cleared) {
                ctx.status(HttpStatus.NO_CONTENT);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Usuario no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de usuario inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al limpiar carrito: " + e.getMessage()));
        }
    }
    
    public void calcularTotalCarrito(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("idUsuario"));
            double total = carritoService.calcularTotalCarrito(idUsuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("total", total);
            response.put("idUsuario", idUsuario);
            
            ctx.json(response);
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de usuario inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al calcular total: " + e.getMessage()));
        }
    }
    
    public void contarItemsCarrito(Context ctx) {
        try {
            int idUsuario = Integer.parseInt(ctx.pathParam("idUsuario"));
            int cantidad = carritoService.contarItemsCarrito(idUsuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("cantidadItems", cantidad);
            response.put("idUsuario", idUsuario);
            
            ctx.json(response);
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de usuario inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al contar items: " + e.getMessage()));
        }
    }
    
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
    
    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> success = new HashMap<>();
        success.put("message", message);
        return success;
    }
}
