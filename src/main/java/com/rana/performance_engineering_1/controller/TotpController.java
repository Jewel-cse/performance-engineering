package com.rana.performance_engineering_1.controller;

import com.rana.performance_engineering_1.dto.TotpSetupResponse;
import com.rana.performance_engineering_1.dto.TotpVerifyRequest;
import com.rana.performance_engineering_1.service.TotpService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/totp")
@RequiredArgsConstructor
public class TotpController {

    private final TotpService totpService;

    /**
     * POST /totp/setup?username=john
     * Registers a user for TOTP and returns the secret + QR code data URI.
     * The QR code can be scanned with Google Authenticator.
     */
    @PostMapping("/setup")
    public ResponseEntity<?> setupTotp(@RequestParam String username) {
        try {
            TotpSetupResponse response = totpService.setupTotp(username);
            return ResponseEntity.ok(response);
        } catch (QrGenerationException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to generate QR code: " + e.getMessage()));
        }
    }

    /**
     * POST /totp/verify
     * Verifies the OTP code provided by the user.
     * Body: { "username": "john", "otp": "123456" }
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody TotpVerifyRequest request) {
        boolean isValid = totpService.verifyOtp(request.getUsername(), request.getOtp());

        if (isValid) {
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "message", "OTP verification successful."
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                    "valid", false,
                    "message", "Invalid OTP. Please try again."
            ));
        }
    }
}
