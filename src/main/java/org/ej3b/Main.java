package org.ej3b;

import io.javalin.Javalin;
import org.ej3b.config.DatabaseConfig;
import org.ej3b.routers.ApiRouter;

public class Main {
    public static void main(String[] args) {
        // Obtener configuración desde variables de entorno
        int serverPort = Integer.parseInt(DatabaseConfig.getDotenv().get("SERVER_PORT", "7000"));
        String serverHost = DatabaseConfig.getDotenv().get("SERVER_HOST", "localhost");
        
        // Crear instancia de Javalin
        Javalin app = Javalin.create(config -> {
            // Javalin 6: usar bundledPlugins para CORS
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> it.anyHost());
            });
        }).start(serverHost, serverPort);
        
        // Configurar rutas
        ApiRouter apiRouter = new ApiRouter(app);
        apiRouter.setupRoutes();
        
        // Configurar manejo de errores
        app.error(404, ctx -> {
            ctx.json("{\"error\": \"Endpoint no encontrado\"}");
        });
        
        app.error(500, ctx -> {
            ctx.json("{\"error\": \"Error interno del servidor\"}");
        });
        
        // Configurar shutdown hook para cerrar conexiones
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cerrando aplicación...");
            app.stop();
            DatabaseConfig.closeDataSource();
        }));
        
        System.out.println("API Miel de la Selva Maya iniciada en " + serverHost + ":" + serverPort);
        System.out.println("Documentación disponible en: http://" + serverHost + ":" + serverPort + "/api/health");
    }
}
