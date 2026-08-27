package com.vehiqon.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenEncryptionService {
    private final TextEncryptor encryptor;

    public String encryptToken(String rawToken) {
        return encryptor.encrypt(rawToken);
    }

    public String decryptToken(String encryptedToken) {
        try {
            return encryptor.decrypt(encryptedToken);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token or tampering detected", e);
        }
    }

}
