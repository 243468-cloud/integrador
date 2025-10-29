package org.ej3b.routers;

import io.javalin.Javalin;
import org.ej3b.controllers.*;

public class ApiRouter {
    private final Javalin app;
    private final UsuarioController usuarioController;
    private final ProductoController productoController;
    private final CarritoController carritoController;
    private final PedidoController pedidoController;
    
    public ApiRouter(Javalin app) {
        this.app = app;
        this.usuarioController = new UsuarioController();
        this.productoController = new ProductoController();
        this.carritoController = new CarritoController();
        this.pedidoController = new PedidoController();
    }
    
    public void setupRoutes() {
        // Configurar CORS
        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });
        
        app.options("/*", ctx -> {
            ctx.status(200);
        });
        
        // Rutas de Usuarios
        app.get("/api/usuarios", usuarioController::getAllUsuarios);
        app.get("/api/usuarios/{id}", usuarioController::getUsuarioById);
        app.post("/api/usuarios", usuarioController::createUsuario);
        app.put("/api/usuarios/{id}", usuarioController::updateUsuario);
        app.delete("/api/usuarios/{id}", usuarioController::deleteUsuario);
        app.post("/api/usuarios/login", usuarioController::login);
        
        // Rutas de Productos
        app.get("/api/productos", productoController::getAllProductos);
        app.get("/api/productos/stock", productoController::getProductosConStock);
        app.get("/api/productos/{id}", productoController::getProductoById);
        app.post("/api/productos", productoController::createProducto);
        app.put("/api/productos/{id}", productoController::updateProducto);
        app.put("/api/productos/{id}/stock", productoController::updateStock);
        app.delete("/api/productos/{id}", productoController::deleteProducto);
        app.post("/api/productos/{id}/verificar-stock", productoController::verificarStock);
        
        // Rutas de Carrito
        app.get("/api/carrito/{idUsuario}", carritoController::getCarritoByUsuario);
        app.post("/api/carrito/{idUsuario}", carritoController::addToCarrito);
        app.put("/api/carrito/{idCarrito}", carritoController::updateCantidadCarrito);
        app.delete("/api/carrito/{idCarrito}", carritoController::removeFromCarrito);
        app.delete("/api/carrito/{idUsuario}/{idProducto}", carritoController::removeFromCarritoByProducto);
        app.delete("/api/carrito/{idUsuario}/clear", carritoController::clearCarrito);
        app.get("/api/carrito/{idUsuario}/total", carritoController::calcularTotalCarrito);
        app.get("/api/carrito/{idUsuario}/count", carritoController::contarItemsCarrito);
        
        // Rutas de Pedidos
        app.get("/api/pedidos", pedidoController::getAllPedidos);
        app.get("/api/pedidos/{id}", pedidoController::getPedidoById);
        app.get("/api/pedidos/usuario/{idUsuario}", pedidoController::getPedidosByUsuario);
        app.get("/api/pedidos/numero/{numeroPedido}", pedidoController::getPedidosByNumero);
        app.get("/api/pedidos/estado/{estado}", pedidoController::getPedidosByEstado);
        app.post("/api/pedidos", pedidoController::createPedido);
        app.post("/api/pedidos/procesar-carrito/{idUsuario}", pedidoController::procesarCarrito);
        app.put("/api/pedidos/{id}", pedidoController::updatePedido);
        app.put("/api/pedidos/{id}/estado", pedidoController::updateEstadoPedido);
        app.get("/api/pedidos/{numeroPedido}/total", pedidoController::calcularTotalPedido);
        app.delete("/api/pedidos/{id}", pedidoController::deletePedido);
        
        // Ruta de salud del API
        app.get("/api/health", ctx -> {
            ctx.json("API Miel de la Selva Maya - Funcionando correctamente");
        });
    }
}
