package org.ej3b.services;

import org.ej3b.models.Usuario;
import org.ej3b.repositories.UsuarioRepository;
import org.ej3b.utils.PasswordUtils;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
    }
    
    public List<Usuario> getAllUsuarios() throws SQLException {
        return usuarioRepository.findAll();
    }
    
    public Usuario getUsuarioById(int id) throws SQLException {
        return usuarioRepository.findById(id);
    }
    
    public Usuario getUsuarioByEmail(String email) throws SQLException {
        return usuarioRepository.findByEmail(email);
    }
    
    public Usuario createUsuario(Usuario usuario) throws SQLException {
        // Hash the password before storing
        String hashedPassword = PasswordUtils.hashPassword(usuario.getContrasena());
        usuario.setContrasena(hashedPassword);
        
        int id = usuarioRepository.create(usuario);
        usuario.setIdUsuario(id);
        return usuario;
    }
    
    public boolean updateUsuario(Usuario usuario) throws SQLException {
        // If password is being updated, hash it
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            String hashedPassword = PasswordUtils.hashPassword(usuario.getContrasena());
            usuario.setContrasena(hashedPassword);
        } else {
            // If password is not being updated, get the existing password
            Usuario existingUsuario = usuarioRepository.findById(usuario.getIdUsuario());
            if (existingUsuario != null) {
                usuario.setContrasena(existingUsuario.getContrasena());
            }
        }
        
        return usuarioRepository.update(usuario);
    }
    
    public boolean deleteUsuario(int id) throws SQLException {
        return usuarioRepository.delete(id);
    }
    
    public boolean authenticateUsuario(String email, String password) throws SQLException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            return PasswordUtils.verifyPassword(password, usuario.getContrasena());
        }
        return false;
    }
    
    public Usuario login(String email, String password) throws SQLException {
        if (authenticateUsuario(email, password)) {
            return usuarioRepository.findByEmail(email);
        }
        return null;
    }
}
