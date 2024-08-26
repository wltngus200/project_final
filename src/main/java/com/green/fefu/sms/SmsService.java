package com.green.fefu.sms;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    public static String generateRandomMessage(int length){
        SecureRandom secureRandom = new SecureRandom();
        String charNSpecialChar = IntStream.rangeClosed(33, 126)
                .mapToObj(i -> String.valueOf((char) i))
                .collect(Collectors.joining());
        StringBuilder builder = new StringBuilder();
        for(int i = 0 ; i < length ; i++){
            builder.append(charNSpecialChar.charAt(secureRandom.nextInt(charNSpecialChar.length())));
        }
        return builder.toString();
    }

    public void sendSms(String to, String from, String text) {
        Message coolsms = new Message(apiKey, apiSecret);

        HashMap<String, String> params = new HashMap<>();
        params.put("to", to);
        params.put("from", from);
        params.put("type", "SMS");
        params.put("text", text);

        try {
            logger.info("Attempting to send SMS: " + params);
            JSONObject obj = coolsms.send(params);
            logger.info("SMS sent successfully. Response: " + obj.toString());
        } catch (CoolsmsException e) {
            logger.error("Failed to send SMS. Error code: " + e.getCode() + ", Error message: " + e.getMessage());
        }
    }

    public void sendPasswordSms(String to, String from, String text) {
        Message coolsms = new Message(apiKey, apiSecret);

        HashMap<String, String> params = new HashMap<>();
        params.put("to", to);
        params.put("from", from);
        params.put("type", "SMS");
        params.put("text", "[Web 발신]\n 본인 확인용 코드입니다. 코드를 입력해주세요.\n 코드: " + text);

        try {
            logger.info("Attempting to send SMS: " + params);
            JSONObject obj = coolsms.send(params);
            logger.info("SMS sent successfully. Response: " + obj.toString());
        } catch (CoolsmsException e) {
            logger.error("Failed to send SMS. Error code: " + e.getCode() + ", Error message: " + e.getMessage());
        }
    }
}