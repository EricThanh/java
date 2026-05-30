package com.football.management.service;

import java.security.SecureRandom;

public class OtpService {
    private static final SecureRandom RANDOM = new SecureRandom();

    public String taoOtp6So() {
        int value = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(value);
    }
}