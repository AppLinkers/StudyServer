package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.service.SMSService;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SMSController {

    private final SMSService smsService;

    @GetMapping("/sendSMS")
    public String sendSMS(@RequestParam(value="to") String to) throws CoolsmsException {
        return smsService.checkPhoneNumber(to);
    }
}
