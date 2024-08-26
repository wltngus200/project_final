package com.green.fefu.sms;

import com.green.fefu.sms.model.SmsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
public class SmsController {
    private final SmsService smsService;
    @Value("${coolsms.api.caller}") private String caller;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send") @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> sendSms(@RequestBody SmsRequest request) {

        smsService.sendSms(request.getTo(), caller, request.getMessage());
        return ResponseEntity.ok("SMS sent successfully");
    }
}