package com.example.studyBridge_server.dto.userMentor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileReq {

    private MultipartFile schoolImg;

    private Optional<List<MultipartFile>> certificatesImg;

    private ProfileTextReq profileTextReq;

}
