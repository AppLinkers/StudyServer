package com.example.studyBridge_server.service;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class SMSService {

    @Value("${coolSMS_API_KEY}")
    private String api_key;

    @Value("${coolSMS_API_SECRET_KEY}")
    private String api_secret_key;

    public String checkPhoneNumber(String to) throws CoolsmsException {

        Message cool_sms = new Message(api_key, api_secret_key);

        Random rand = new Random();

        String numStr = "";

        for (int i = 0; i < 4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("to", to);    // 수신전화번호 (ajax로 view 화면에서 받아온 값으로 넘김)
        params.put("from", "01063026304");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "[StudyBridge] 인증번호는 [" + numStr + "] 입니다.");

        JSONObject result = cool_sms.send(params);

        return numStr;
    }
}
