package com.project.English365.security;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class OTPGenerator {

    private static final int OTP_LENGTH = 6;

    public String generateOTP() {
        String numbers = "0123456789";
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        Random random = new Random();

        for (int i = 0; i < OTP_LENGTH; i++) {
            int randomIndex = random.nextInt(numbers.length());
            otp.append(numbers.charAt(randomIndex));
        }

        return otp.toString();
    }
}
