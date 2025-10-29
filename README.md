# API RESTful - Miel de la Selva Maya

Esta es una API RESTful desarrollada con Javalin para el e-commerce de "Miel de la Selva Maya", implementando todos los requisitos funcionales especificados.

## Arquitectura

El proyecto sigue una arquitectura en capas:

```
src/main/java/org/ej3b/
├── config/          # Configuración de base de datos
├── controllers/     # Controladores REST
├── models/          # Modelos de datos (POJOs)
├── repositories/    # Capa de acceso a datos
├── routers/         # Configuración de rutas
├── services/        # Lógica de negocio
└── Main.java        # Punto de entrada de la aplicación
```

## Tecnologías Utilizadas

- **Javalin 6.7.0**: Framework web ligero para Java
- **MySQL**: Base de datos relacional
- **HikariCP**: Pool de conexiones de alto rendimiento
- **Password4j**: Hashing seguro de contraseñas
- **JWT**: Autenticación con tokens (preparado para implementación)
- **Gradle**: Sistema de construcción

## Configuración de Base de Datos

1. **Instalar MySQL Server**
2. **Ejecutar el script SQL**:
   ```sql
   mysql -u root -p < database_schema.sql
   ```
3. **Configurar variables de entorno**:
   - Copia el archivo `env.example` a `.env`
   - Edita `.env` con tus credenciales de base de datos:
   ```env
   DB_HOST=localhost
   DB_PORT=3306
   DB_NAME=miel_selva_maya
   DB_USERNAME=tu_usuario
   DB_PASSWORD=tu_contraseña
   ```

## Instalación y Ejecución

### Método 1: Scripts Automáticos (Recomendado)

**Windows:**
```cmd
start-api.bat
```

**Linux/Mac:**
```bash
./start-api.sh
```

### Método 2: Manual

1. **Configurar variables de entorno**:
   ```bash
   cp env.example .env
   # Editar .env con tus credenciales
   ```

2. **Compilar el proyecto**:
   ```bash
   ./gradlew build
   ```

3. **Ejecutar la aplicación**:
   ```bash
   ./gradlew run
   ```

La API estará disponible en: `http://localhost:7000`

### Verificar que funciona:
```bash
curl http://localhost:7000/api/health
```

## Endpoints de la API

### Usuarios
- `GET /api/usuarios` - Obtener todos los usuarios
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `POST /api/usuarios` - Crear nuevo usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario
- `POST /api/usuarios/login` - Autenticar usuario

### Productos
- `GET /api/productos` - Obtener todos los productos
- `GET /api/productos/stock` - Obtener productos con stock
- `GET /api/productos/{id}` - Obtener producto por ID
- `POST /api/productos` - Crear nuevo producto
- `PUT /api/productos/{id}` - Actualizar producto
- `PUT /api/productos/{id}/stock` - Actualizar stock
- `DELETE /api/productos/{id}` - Eliminar producto
- `POST /api/productos/{id}/verificar-stock` - Verificar disponibilidad

### Carrito de Compras
- `GET /api/carrito/{idUsuario}` - Obtener carrito del usuario
- `POST /api/carrito/{idUsuario}` - Agregar producto al carrito
- `PUT /api/carrito/{idCarrito}` - Actualizar cantidad
- `DELETE /api/carrito/{idCarrito}` - Eliminar item del carrito
- `DELETE /api/carrito/{idUsuario}/{idProducto}` - Eliminar producto específico
- `DELETE /api/carrito/{idUsuario}/clear` - Limpiar carrito
- `GET /api/carrito/{idUsuario}/total` - Calcular total del carrito
- `GET /api/carrito/{idUsuario}/count` - Contar items en carrito

### Pedidos
- `GET /api/pedidos` - Obtener todos los pedidos
- `GET /api/pedidos/{id}` - Obtener pedido por ID
- `GET /api/pedidos/usuario/{idUsuario}` - Obtener pedidos del usuario
- `GET /api/pedidos/numero/{numeroPedido}` - Obtener pedidos por número
- `GET /api/pedidos/estado/{estado}` - Obtener pedidos por estado
- `POST /api/pedidos` - Crear nuevo pedido
- `POST /api/pedidos/procesar-carrito/{idUsuario}` - Procesar carrito completo
- `PUT /api/pedidos/{id}` - Actualizar pedido
- `PUT /api/pedidos/{id}/estado` - Actualizar estado del pedido
- `GET /api/pedidos/{numeroPedido}/total` - Calcular total del pedido
- `DELETE /api/pedidos/{id}` - Eliminar pedido

### Salud del Sistema
- `GET /api/health` - Verificar estado de la API

## Características Implementadas

### ✅ Requisitos Funcionales Cumplidos

1. **Gestión de Catálogo y Producto**
   - Subida, edición y eliminación de productos
   - Catálogo accesible 24/7 sin autenticación
   - Precios diferenciados para menudeo y mayoreo

2. **Proceso de Compra y Pedido**
   - Carrito de compras temporal
   - Cálculo automático de totales con descuentos
   - Integración preparada para pasarelas de pago
   - Generación de pedidos con identificadores únicos

3. **Gestión de Inventario**
   - Reducción automática de stock
   - Validación de disponibilidad antes del pago
   - Actualización manual de stock

4. **Gestión de Usuarios**
   - Registro y autenticación
   - Múltiples direcciones por usuario
   - Historial de pedidos

5. **Gestión Administrativa**
   - Consulta y filtrado de pedidos
   - Actualización de estados
   - Reportes de ventas (preparado para exportación)

6. **Comunicación**
   - Sistema preparado para notificaciones por email
   - Alertas para administradores

## Descuentos por Volumen

- **Mayoreo (10+ unidades)**: 15% de descuento
- **Mediano (5-9 unidades)**: 10% de descuento
- **Menudeo (1-4 unidades)**: Sin descuento

## Seguridad

- **Variables de entorno**: Credenciales protegidas en archivo `.env`
- **Contraseñas hasheadas**: BCrypt para almacenamiento seguro
- **Validación de entrada**: En todos los endpoints
- **Manejo de errores**: Centralizado y seguro
- **CORS**: Configurado para desarrollo
- **Pool de conexiones**: HikariCP para conexiones seguras y eficientes

### ⚠️ Importante - Seguridad

1. **NUNCA commitees el archivo `.env`** al repositorio
2. **El archivo `.env` está en `.gitignore`** para protección
3. **Usa `env.example`** como plantilla para otros desarrolladores
4. **Cambia las contraseñas por defecto** en producción

## Próximos Pasos

1. Implementar autenticación JWT completa
2. Integrar pasarela de pagos real
3. Implementar sistema de notificaciones por email
4. Agregar validaciones de negocio adicionales
5. Implementar logging y monitoreo
6. Crear documentación API con Swagger/OpenAPI

## Contribución

Para contribuir al proyecto:
1. Fork el repositorio
2. Crear una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Crear un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT.
# api_integrador
