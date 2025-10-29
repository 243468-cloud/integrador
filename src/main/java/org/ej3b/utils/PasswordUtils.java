package org.ej3b.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilidad para manejar el hash y verificación de contraseñas
 * como alternativa a Password4j
 */
public class PasswordUtils {
    private static final int SALT_LENGTH = 16;
    private static final String ALGORITHM = "SHA-256";
    private static final String SEPARATOR = "$";
    
    /**
     * Genera un hash de la contraseña usando SHA-256 con salt
     * @param password La contraseña a hashear
     * @return String con el hash en formato salt$hash
     */
    public static String hashPassword(String password) {
        try {
            // Generar salt aleatorio
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Crear hash con salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // Codificar salt y hash en Base64
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hashedPassword);
            
            // Combinar salt y hash
            return saltBase64 + SEPARATOR + hashBase64;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }
    
    /**
     * Verifica si una contraseña coincide con un hash almacenado
     * @param password La contraseña a verificar
     * @param storedHash El hash almacenado en formato salt$hash
     * @return true si la contraseña coincide, false en caso contrario
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Separar salt y hash
            String[] parts = storedHash.split("\\$");
            if (parts.length != 2) {
                return false;
            }
            
            // Decodificar salt y hash
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);
            
            // Recrear hash con la contraseña proporcionada
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] newHash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // Comparar hashes
            int diff = hash.length ^ newHash.length;
            for (int i = 0; i < hash.length && i < newHash.length; i++) {
                diff |= hash[i] ^ newHash[i];
            }
            
            return diff == 0;
        } catch (Exception e) {
            return false;
        }
    }
}