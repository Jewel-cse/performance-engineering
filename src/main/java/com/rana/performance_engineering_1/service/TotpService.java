package com.rana.performance_engineering_1.service;

import com.rana.performance_engineering_1.dto.TotpSetupResponse;
import com.rana.performance_engineering_1.model.TotpUser;
import com.rana.performance_engineering_1.repository.TotpUserRepository;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
@RequiredArgsConstructor
public class TotpService {

    private final TotpUserRepository totpUserRepository;

    /**
     * Registers a new user and generates a TOTP secret.
     * Returns the secret and a QR code URI that can be scanned by Google Authenticator.
     */
    public TotpSetupResponse setupTotp(String username) throws QrGenerationException {
        // Check if user already exists
        Optional<TotpUser> existingUser = totpUserRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            // Re-generate setup info for existing user
            String secret = existingUser.get().getSecret();
            String qrCodeUri = generateQrCodeUri(username, secret);
            return new TotpSetupResponse(username, secret, qrCodeUri,
                    "User already registered. Use the existing secret or scan the QR code again.");
        }

        // Generate a new secret
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();

        // Save user with secret
        TotpUser totpUser = new TotpUser(username, secret);
        totpUserRepository.save(totpUser);

        // Generate QR code URI
        String qrCodeUri = generateQrCodeUri(username, secret);

        return new TotpSetupResponse(username, secret, qrCodeUri,
                "TOTP setup successful. Scan the QR code with Google Authenticator.");
    }

    /**
     * Verifies the OTP code provided by the user.
     */
    public boolean verifyOtp(String username, String otp) {
        Optional<TotpUser> totpUser = totpUserRepository.findByUsername(username);
        if (totpUser.isEmpty()) {
            return false;
        }

        String secret = totpUser.get().getSecret();

        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        return verifier.isValidCode(secret, otp);
    }

    /**
     * Checks if a user is registered for TOTP.
     */
    public boolean isUserRegistered(String username) {
        return totpUserRepository.findByUsername(username).isPresent();
    }

    /**
     * Generates a QR code data URI for scanning with Google Authenticator.
     */
    private String generateQrCodeUri(String username, String secret) throws QrGenerationException {
        QrData qrData = new QrData.Builder()
                .label(username)
                .secret(secret)
                .issuer("PerformanceEngineeringApp")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        ZxingPngQrGenerator qrGenerator = new ZxingPngQrGenerator();
        byte[] imageData = qrGenerator.generate(qrData);

        return getDataUriForImage(imageData, qrGenerator.getImageMimeType());
    }
}
