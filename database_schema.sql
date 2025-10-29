-- Script SQL para crear la base de datos "Miel de la Selva Maya"
-- Basado en el esquema ERD proporcionado

CREATE DATABASE IF NOT EXISTS miel_selva_maya;
USE miel_selva_maya;

-- Tabla de Roles
CREATE TABLE IF NOT EXISTS Rol (
    ID_Rol INT AUTO_INCREMENT PRIMARY KEY,
    Nombre_Rol VARCHAR(50) NOT NULL
);

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS Usuario (
    ID_Usuario INT AUTO_INCREMENT PRIMARY KEY,
    Nombre_Completo VARCHAR(255) NOT NULL,
    Correo VARCHAR(100) NOT NULL UNIQUE,
    Contrasena VARCHAR(255) NOT NULL,
    Num_Celular VARCHAR(20),
    ID_Rol INT NOT NULL,
    FOREIGN KEY (ID_Rol) REFERENCES Rol(ID_Rol)
);

-- Tabla de Métodos de Pago
CREATE TABLE IF NOT EXISTS Metodo_Pago (
    ID_Metodo_Pago INT AUTO_INCREMENT PRIMARY KEY,
    ID_Usuario INT NOT NULL,
    Tipo VARCHAR(50) NOT NULL,
    Detalles VARCHAR(255),
    FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario)
);

-- Tabla de Direcciones
CREATE TABLE IF NOT EXISTS Direccion (
    ID_Direccion INT AUTO_INCREMENT PRIMARY KEY,
    ID_Usuario INT NOT NULL,
    Calle VARCHAR(255) NOT NULL,
    Colonia VARCHAR(100),
    Codigo_Postal VARCHAR(10) NOT NULL,
    Ciudad VARCHAR(100) NOT NULL,
    Estado VARCHAR(100) NOT NULL,
    FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario)
);

-- Tabla de Productos
CREATE TABLE IF NOT EXISTS Producto (
    ID_Producto INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(255) NOT NULL,
    Descripcion TEXT,
    Precio DECIMAL(10,2) NOT NULL,
    Stock INT NOT NULL,
    Imagen VARCHAR(500)
);

-- Tabla de Carrito
CREATE TABLE IF NOT EXISTS Carrito (
    ID_Carrito INT AUTO_INCREMENT PRIMARY KEY,
    ID_Usuario INT NOT NULL,
    ID_Producto INT NOT NULL,
    Cantidad INT NOT NULL,
    FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario),
    FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

-- Tabla de Pedidos
CREATE TABLE IF NOT EXISTS Pedido (
    ID_Pedido INT AUTO_INCREMENT PRIMARY KEY,
    Numero_Pedido VARCHAR(50) NOT NULL,
    ID_Usuario INT NOT NULL,
    ID_Producto INT NOT NULL,
    Cantidad INT NOT NULL,
    ID_Metodo_Pago INT NOT NULL,
    ID_Direccion INT NOT NULL,
    Total DECIMAL(10,2) NOT NULL,
    Estado VARCHAR(50),
    Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario),
    FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto),
    FOREIGN KEY (ID_Metodo_Pago) REFERENCES Metodo_Pago(ID_Metodo_Pago),
    FOREIGN KEY (ID_Direccion) REFERENCES Direccion(ID_Direccion)
);

-- Tabla de Envíos
CREATE TABLE IF NOT EXISTS Envio (
    ID_Envio INT AUTO_INCREMENT PRIMARY KEY,
    ID_Pedido INT NOT NULL,
    Numero_Guia VARCHAR(180),
    Estado VARCHAR(50),
    Fecha_Envio TIMESTAMP,
    FOREIGN KEY (ID_Pedido) REFERENCES Pedido(ID_Pedido)
);

-- Tabla de Reseñas
CREATE TABLE IF NOT EXISTS Resena (
    ID_Resena INT AUTO_INCREMENT PRIMARY KEY,
    ID_Producto INT NOT NULL,
    comentario VARCHAR(500),
    FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

-- Insertar datos iniciales

-- Roles
INSERT INTO Rol (Nombre_Rol) VALUES 
('Cliente'),
('Administrador'),
('Distribuidor');

-- Productos de ejemplo
INSERT INTO Producto (Nombre, Descripcion, Precio, Stock, Imagen) VALUES 
('Miel de Abeja Natural', 'Miel pura de abeja recolectada de la selva maya', 150.00, 50, 'miel_natural.jpg'),
('Miel de Acacia', 'Miel de acacia con sabor suave y delicado', 180.00, 30, 'miel_acacia.jpg'),
('Miel de Eucalipto', 'Miel de eucalipto con propiedades medicinales', 200.00, 25, 'miel_eucalipto.jpg'),
('Miel Multifloral', 'Mezcla de diferentes flores de la región', 160.00, 40, 'miel_multifloral.jpg'),
('Miel de Orégano', 'Miel con propiedades antibióticas naturales', 220.00, 20, 'miel_oregano.jpg');

-- Usuario administrador por defecto
INSERT INTO Usuario (Nombre_Completo, Correo, Contrasena, Num_Celular, ID_Rol) VALUES 
('Administrador Sistema', 'admin@mielselvamaya.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '5551234567', 2);

-- Crear índices para mejorar el rendimiento
CREATE INDEX idx_usuario_correo ON Usuario(Correo);
CREATE INDEX idx_pedido_numero ON Pedido(Numero_Pedido);
CREATE INDEX idx_pedido_usuario ON Pedido(ID_Usuario);
CREATE INDEX idx_pedido_estado ON Pedido(Estado);
CREATE INDEX idx_carrito_usuario ON Carrito(ID_Usuario);
CREATE INDEX idx_producto_stock ON Producto(Stock);
