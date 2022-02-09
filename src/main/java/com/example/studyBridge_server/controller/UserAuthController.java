package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.domaion.ErrorMessage;
import com.example.studyBridge_server.dto.UserAuth.UserLoginReq;
import com.example.studyBridge_server.dto.UserAuth.UserSignUpReq;
import com.example.studyBridge_server.dto.UserAuth.UserSignUpRes;
import com.example.studyBridge_server.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    // 회원가입
    @PostMapping("/sign")
    public ResponseEntity<UserSignUpRes> create(@ModelAttribute UserSignUpReq userSignUpReq) {
        return ResponseEntity.status(201).body(userAuthService.create(userSignUpReq));
    }

    // 로그인
    @PostMapping("/")
    public ResponseEntity login(@RequestBody UserLoginReq userLoginReq) {

        try {
            return ResponseEntity.ok().body(userAuthService.login(userLoginReq));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }

    }
}
