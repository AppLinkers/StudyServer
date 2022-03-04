package com.example.studyBridge_server.dto.userMentor;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Data
public class ProfileReq {

    private MultipartFile schoolImg;

    private Optional<List<MultipartFile>> certificatesImg;

    private ProfileTextReq profileTextReq;

}
