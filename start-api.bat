@echo off
echo ========================================
echo   API Miel de la Selva Maya
echo ========================================
echo.

REM Verificar si existe el archivo .env
if not exist ".env" (
    echo ERROR: No se encontro el archivo .env
    echo.
    echo Por favor:
    echo 1. Copia el archivo env.example a .env
    echo 2. Edita .env con tus credenciales de base de datos
    echo 3. Ejecuta este script nuevamente
    echo.
    pause
    exit /b 1
)

echo Verificando conexion a la base de datos...
echo.

REM Compilar el proyecto
echo Compilando proyecto...
call gradlew build
if %errorlevel% neq 0 (
    echo ERROR: Fallo la compilacion
    pause
    exit /b 1
)

echo.
echo Iniciando API...
echo La API estara disponible en: http://localhost:7000
echo Presiona Ctrl+C para detener el servidor
echo.

REM Ejecutar la aplicacion
call gradlew run
