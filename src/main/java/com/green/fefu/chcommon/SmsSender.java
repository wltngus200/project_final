package com.green.fefu.chcommon;

import java.security.SecureRandom;

public class SmsSender {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static void sendSms(String phoneNumber, String message) {

        String msg = "<p>비밀번호 변경 인증코드</p>" +
                     "<p>" + message + "</p>";
    }
    public static String makeRandomCode() {
        return Integer.toString(SECURE_RANDOM.nextInt(900000) + 100000);
    }
}
