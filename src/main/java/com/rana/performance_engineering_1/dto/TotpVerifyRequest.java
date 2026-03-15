package com.rana.performance_engineering_1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotpVerifyRequest {
    private String username;
    private String otp;
}
