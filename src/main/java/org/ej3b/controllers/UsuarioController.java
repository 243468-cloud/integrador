package org.ej3b.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.ej3b.models.Usuario;
import org.ej3b.services.UsuarioService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioController {
    private final UsuarioService usuarioService;
    
    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }
    
    public void getAllUsuarios(Context ctx) {
        try {
            List<Usuario> usuarios = usuarioService.getAllUsuarios();
            ctx.json(usuarios);
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al obtener usuarios: " + e.getMessage()));
        }
    }
    
    public void getUsuarioById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Usuario usuario = usuarioService.getUsuarioById(id);
            
            if (usuario != null) {
                ctx.json(usuario);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Usuario no encontrado"));
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de usuario inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al obtener usuario: " + e.getMessage()));
        }
    }
    
    public void createUsuario(Context ctx) {
        try {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            
            // Validaciones básicas
            if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El nombre completo es requerido"));
                return;
            }
            
            if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El correo es requerido"));
                return;
            }
            
            if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("La contraseña es requerida"));
                return;
            }
            
            // Verificar si el usuario ya existe
            Usuario existingUsuario = usuarioService.getUsuarioByEmail(usuario.getCorreo());
            if (existingUsuario != null) {
                ctx.status(HttpStatus.CONFLICT).json(createErrorResponse("Ya existe un usuario con este correo"));
                return;
            }
            
            // Establecer rol por defecto (cliente = 1)
            if (usuario.getIdRol() == 0) {
                usuario.setIdRol(1);
            }
            
            Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
            ctx.status(HttpStatus.CREATED).json(nuevoUsuario);
            
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al crear usuario: " + e.getMessage()));
        }
    }
    
    public void updateUsuario(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            usuario.setIdUsuario(id);
            
            boolean updated = usuarioService.updateUsuario(usuario);
            
            if (updated) {
                Usuario usuarioActualizado = usuarioService.getUsuarioById(id);
                ctx.json(usuarioActualizado);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Usuario no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de usuario inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al actualizar usuario: " + e.getMessage()));
        }
    }
    
    public void deleteUsuario(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean deleted = usuarioService.deleteUsuario(id);
            
            if (deleted) {
                ctx.status(HttpStatus.NO_CONTENT);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json(createErrorResponse("Usuario no encontrado"));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("ID de usuario inválido"));
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error al eliminar usuario: " + e.getMessage()));
        }
    }
    
    public void login(Context ctx) {
        try {
            Map<String, String> loginData = ctx.bodyAsClass(Map.class);
            String email = loginData.get("email");
            String password = loginData.get("password");
            
            if (email == null || email.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("El correo es requerido"));
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST).json(createErrorResponse("La contraseña es requerida"));
                return;
            }
            
            Usuario usuario = usuarioService.login(email, password);
            
            if (usuario != null) {
                // En un caso real, aquí generarías un JWT token
                Map<String, Object> response = new HashMap<>();
                response.put("usuario", usuario);
                response.put("message", "Login exitoso");
                ctx.json(response);
            } else {
                ctx.status(HttpStatus.UNAUTHORIZED).json(createErrorResponse("Credenciales inválidas"));
            }
            
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(createErrorResponse("Error en el login: " + e.getMessage()));
        }
    }
    
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
