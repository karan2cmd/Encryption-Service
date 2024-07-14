package com.greatkapital.encryptionservice.controller;
import com.greatkapital.encryptionservice.dto.EncryptionRequestDto;
import com.greatkapital.encryptionservice.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EncryptionController {
    @Autowired
    EncryptionService encryptionService;

    @PostMapping("/encrypt")
    public ResponseEntity<?> encryptMessage(@RequestBody EncryptionRequestDto request) {
        ResponseEntity<?> response = encryptionService.encryptMessage(request);
        return response;
    }

}
