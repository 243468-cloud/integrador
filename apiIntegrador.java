#!/bin/bash

echo "========================================"
echo "   API Miel de la Selva Maya"
echo "========================================"
echo

# Verificar si existe el archivo .env
if [ ! -f ".env" ]; then
    echo "ERROR: No se encontró el archivo .env"
    echo
    echo "Por favor:"
    echo "1. Copia el archivo env.example a .env"
    echo "2. Edita .env con tus credenciales de base de datos"
    echo "3. Ejecuta este script nuevamente"
    echo
    exit 1
fi

echo "Verificando conexión a la base de datos..."
echo

# Compilar el proyecto
echo "Compilando proyecto..."
./gradlew build
if [ $? -ne 0 ]; then
    echo "ERROR: Falló la compilación"
    exit 1
fi

echo
echo "Iniciando API..."
echo "La API estará disponible en: http://localhost:7000"
echo "Presiona Ctrl+C para detener el servidor"
echo

# Ejecutar la aplicación
./gradlew run
