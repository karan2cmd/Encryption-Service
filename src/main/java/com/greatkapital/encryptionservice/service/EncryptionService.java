package com.greatkapital.encryptionservice.service;

import com.greatkapital.encryptionservice.dto.EncryptionRequestDto;
import com.greatkapital.encryptionservice.dto.EncryptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EncryptionService {

    private static final String ALGORITHM = "AES";

    @Value("${secret.key:TheBestsecretKey}")
    private String keyValue;
    private HttpHeaders headers;

    public ResponseEntity<?> encryptMessage(EncryptionRequestDto request) {
        String originalMessage = request.getMessage();

        try {
            SecretKey key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(originalMessage.getBytes());
            String encryptedValue = Base64.getEncoder().encodeToString(encVal);

            Map<String, String> response = new HashMap<>();
            EncryptionResponseDto encryptionResponseDto = EncryptionResponseDto.builder().encryptedMessage(encryptedValue).build();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            return ResponseEntity.
                    status(200).
                    headers(headers).
                    body(encryptionResponseDto);
        } catch (Exception e) {
            log.error("Exception occur while encrypting message with error ", e);
            return ResponseEntity.
                    status(500).body("Exception occur while encrypting message");
        }

    }

    private SecretKey generateKey() {
        byte[] keyValue = this.keyValue.getBytes();
        return new SecretKeySpec(keyValue, ALGORITHM);
    }
}
