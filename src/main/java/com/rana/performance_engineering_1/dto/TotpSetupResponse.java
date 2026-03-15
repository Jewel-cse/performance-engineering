package com.rana.performance_engineering_1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotpSetupResponse {
    private String username;
    private String secret;
    private String qrCodeUri;
    private String message;
}
