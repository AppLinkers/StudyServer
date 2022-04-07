package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.domain.ErrorMessage;
import com.example.studyBridge_server.dto.userAuth.UserLoginReq;
import com.example.studyBridge_server.dto.userAuth.UserSignUpReq;
import com.example.studyBridge_server.dto.userAuth.UserSignUpRes;
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
    public ResponseEntity<UserSignUpRes> create(@RequestBody UserSignUpReq userSignUpReq) {
        return ResponseEntity.status(201).body(userAuthService.create(userSignUpReq));
    }

    // 로그인
    @PostMapping("/")
    public ResponseEntity login(@RequestBody UserLoginReq userLoginReq) {

        try {
            return ResponseEntity.status(201).body(userAuthService.login(userLoginReq));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    // ID 중복 체크
    @GetMapping("/id")
    public ResponseEntity<String> valid(@RequestParam String userLoginId) {
        if (userAuthService.IdValidChk(userLoginId)) {
            return ResponseEntity.status(201).body("success");
        } else {
            return ResponseEntity.badRequest().body("not valid");
        }
    }

    // Mentee 확인
    @GetMapping("/role")
    public ResponseEntity<Boolean> isMentee(@RequestParam String userLoginId) {
        if (userAuthService.isMentee(userLoginId)) {
            return ResponseEntity.status(201).body(true);
        } else {
            return ResponseEntity.status(201).body(false);
        }
    }
}
