package com.greatkapital.encryptionservice.service;

import com.greatkapital.encryptionservice.dto.DecryptionRequestDto;
import com.greatkapital.encryptionservice.dto.DecryptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
@Deprecated
@Service
@Slf4j
public class DecryptionService {

    private static final String ALGORITHM = "AES";

    @Value("${secret.key:TheBestsecretKey}")
    private String keyValue;
    private HttpHeaders headers;


    public ResponseEntity<?> decryptMessage(DecryptionRequestDto request) {
        String encryptedMessage = request.getEncryptedMessage();
        try {
            SecretKey key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] encVal = Base64.getDecoder().decode(encryptedMessage);
            byte[] decVal = c.doFinal(encVal);
            String decryptedString = new String(decVal, StandardCharsets.UTF_8);
            DecryptionResponseDto decryptionResponseDto = DecryptionResponseDto.builder().decryptedMessage(decryptedString).build();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            return ResponseEntity.
                    status(200).
                    headers(headers).
                    body(decryptionResponseDto);

        } catch (Exception e) {
            log.error("Exception occur while decrypting message with error ", e);
            return ResponseEntity.
                    status(500).body("Exception occur while decrypting message");
        }
    }

    private SecretKey generateKey() {
        byte[] keyValue = this.keyValue.getBytes();
        return new SecretKeySpec(keyValue, ALGORITHM);
    }
}
