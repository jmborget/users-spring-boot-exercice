package com.axpe.example.service.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class PBKDF2 { //org.springframework.security.crypto.bcrypt mejorar
    
    public Optional<String> generate(String password) {
        try {
            int iterations = 1000;
            char[] chars = password.toCharArray();
            byte[] salt = getSalt();
            
            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Optional.of(iterations + ":" + toHex(salt) + ":" + toHex(hash));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    
    private String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
    
    public boolean validate(String originalPassword, String storedPassword) {
        
        try {
            String[] parts = storedPassword.split(":");
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = fromHex(parts[1]);
            byte[] hash = fromHex(parts[2]);
            
            PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations,
                    hash.length * 8);
            
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            
            byte[] testHash = skf.generateSecret(spec).getEncoded();
            
            int diff = hash.length ^ testHash.length;
            for (int i = 0; i < hash.length && i < testHash.length; i++) {
                diff |= hash[i] ^ testHash[i];
            }
            
            return diff == 0;
            
        } catch (NumberFormatException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            
            return false;
        }
    }
    
    private byte[] fromHex(String hex) {
        
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        
        return bytes;
    }
    
}
