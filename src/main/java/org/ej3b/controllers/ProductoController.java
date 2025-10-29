package org.ej3b.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.ej3b.models.Producto;
import org.ej3b.services.ProductoService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {
    private final ProductoService productoService;
    
    public ProductoController() {
        this.productoService = new ProductoService();
    }
    
    public void getAllProductos(Context ctx) {
        try {
            List<Producto> productos = productoService.getAllProductos();
            ctx.json(productos);
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al obtener productos: " + e.getMessage()));
        }
    }
    
    public void getProductosConStock(Context ctx) {
        try {
            List<Producto> productos = productoService.getProductosConStock();
            ctx.json(productos);
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al obtener productos con stock: " + e.getMessage()));
        }
    }
    
    public void getProductoById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Producto producto = productoService.getProductoById(id);
            
            if (producto != null) {
                ctx.json(producto);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Producto no encontrado"));
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de producto inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al obtener producto: " + e.getMessage()));
        }
    }
    
    public void createProducto(Context ctx) {
        try {
            Producto producto = ctx.bodyAsClass(Producto.class);
            
            // Validaciones básicas
            if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El nombre del producto es requerido"));
                return;
            }
            
            if (producto.getPrecio() <= 0) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El precio debe ser mayor a 0"));
                return;
            }
            
            if (producto.getStock() < 0) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El stock no puede ser negativo"));
                return;
            }
            
            Producto nuevoProducto = productoService.createProducto(producto);
            ctx.status(HttpStatus.CREATED).json(nuevoProducto);
            
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al crear producto: " + e.getMessage()));
        }
    }
    
    public void updateProducto(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Producto producto = ctx.bodyAsClass(Producto.class);
            producto.setIdProducto(id);
            
            boolean updated = productoService.updateProducto(producto);
            
            if (updated) {
                Producto productoActualizado = productoService.getProductoById(id);
                ctx.json(productoActualizado);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Producto no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de producto inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al actualizar producto: " + e.getMessage()));
        }
    }
    
    public void updateStock(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Map<String, Integer> stockData = ctx.bodyAsClass(Map.class);
            Integer newStock = stockData.get("stock");
            
            if (newStock == null) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El campo 'stock' es requerido"));
                return;
            }
            
            if (newStock < 0) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El stock no puede ser negativo"));
                return;
            }
            
            boolean updated = productoService.updateStock(id, newStock);
            
            if (updated) {
                Producto producto = productoService.getProductoById(id);
                ctx.json(producto);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Producto no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de producto inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al actualizar stock: " + e.getMessage()));
        }
    }
    
    public void deleteProducto(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean deleted = productoService.deleteProducto(id);
            
            if (deleted) {
                ctx.status(HttpStatus.NO_CONTENT);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Producto no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de producto inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al eliminar producto: " + e.getMessage()));
        }
    }
    
    public void verificarStock(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Map<String, Integer> requestData = ctx.bodyAsClass(Map.class);
            Integer cantidad = requestData.get("cantidad");
            
            if (cantidad == null) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El campo 'cantidad' es requerido"));
                return;
            }
            
            boolean tieneStock = productoService.verificarStock(id, cantidad);
            
            Map<String, Object> response = new HashMap<>();
            response.put("tieneStock", tieneStock);
            response.put("productoId", id);
            response.put("cantidadSolicitada", cantidad);
            
            ctx.json(response);
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de producto inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al verificar stock: " + e.getMessage()));
        }
    }
    
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
