package org.ej3b.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    private static HikariDataSource dataSource;
    private static Dotenv dotenv;

    static {
        // Cargar variables de entorno desde .env
        dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        HikariConfig config = new HikariConfig();
        
        // Configuración desde variables de entorno sin valores por defecto
        String host = dotenv.get("DB_HOST");
        String port = dotenv.get("DB_PORT");
        String database = dotenv.get("DB_NAME");
        String username = dotenv.get("DB_USERNAME");
        String password = dotenv.get("DB_PASSWORD");
        
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        
        // Configuración del pool desde variables de entorno
        config.setMaximumPoolSize(Integer.parseInt(dotenv.get("DB_MAX_POOL_SIZE", "10")));
        config.setMinimumIdle(Integer.parseInt(dotenv.get("DB_MIN_IDLE", "5")));
        config.setConnectionTimeout(Long.parseLong(dotenv.get("DB_CONNECTION_TIMEOUT", "30000")));
        config.setIdleTimeout(Long.parseLong(dotenv.get("DB_IDLE_TIMEOUT", "600000")));
        config.setMaxLifetime(Long.parseLong(dotenv.get("DB_MAX_LIFETIME", "1800000")));
        
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
    
    public static Dotenv getDotenv() {
        return dotenv;
    }
}
