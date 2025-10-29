# 🚀 Guía Rápida - API Miel de la Selva Maya

## ⚡ Inicio Rápido

### 1. Configurar Base de Datos
```bash
# Ejecutar script SQL
mysql -u root -p < database_schema.sql
```

### 2. Configurar Variables de Entorno
```bash
# Copiar archivo de ejemplo
cp env.example .env

# Editar .env con tus credenciales
# Cambiar: DB_PASSWORD=tu_contraseña_aqui
```

### 3. Ejecutar API

**Windows:**
```cmd
start-api.bat
```

**Linux/Mac:**
```bash
./start-api.sh
```

### 4. Verificar que funciona
```bash
curl http://localhost:7000/api/health
```

## 📋 Endpoints Principales

### Productos (Público)
```bash
# Ver catálogo
GET http://localhost:7000/api/productos

# Ver productos con stock
GET http://localhost:7000/api/productos/stock
```

### Usuarios
```bash
# Registrar usuario
POST http://localhost:7000/api/usuarios
{
  "nombreCompleto": "Juan Pérez",
  "correo": "juan@email.com",
  "contrasena": "password123",
  "numCelular": "5551234567",
  "idRol": 1
}

# Login
POST http://localhost:7000/api/usuarios/login
{
  "email": "juan@email.com",
  "password": "password123"
}
```

### Carrito
```bash
# Ver carrito del usuario
GET http://localhost:7000/api/carrito/1

# Agregar al carrito
POST http://localhost:7000/api/carrito/1
{
  "idProducto": 1,
  "cantidad": 2
}
```

### Pedidos
```bash
# Procesar carrito completo
POST http://localhost:7000/api/pedidos/procesar-carrito/1
{
  "idMetodoPago": 1,
  "idDireccion": 1
}

# Ver pedidos del usuario
GET http://localhost:7000/api/pedidos/usuario/1
```

## 🔧 Configuración Avanzada

### Variables de Entorno (.env)
```env
# Base de datos
DB_HOST=localhost
DB_PORT=3306
DB_NAME=miel_selva_maya
DB_USERNAME=root
DB_PASSWORD=tu_contraseña

# Servidor
SERVER_PORT=7000
SERVER_HOST=localhost

# Pool de conexiones
DB_MAX_POOL_SIZE=10
DB_MIN_IDLE=5
```

## 🛠️ Comandos Útiles

```bash
# Compilar proyecto
./gradlew build

# Ejecutar tests
./gradlew test

# Limpiar build
./gradlew clean

# Ver dependencias
./gradlew dependencies
```

## 🚨 Solución de Problemas

### Error de conexión a BD
1. Verificar que MySQL esté ejecutándose
2. Revisar credenciales en `.env`
3. Verificar que la base de datos existe

### Puerto ocupado
1. Cambiar `SERVER_PORT` en `.env`
2. O matar proceso: `netstat -ano | findstr :7000`

### Error de compilación
1. Verificar Java 11+
2. Limpiar: `./gradlew clean build`

## 📞 Soporte

- **Documentación**: README.md
- **Health Check**: http://localhost:7000/api/health
- **Logs**: Consola de la aplicación
