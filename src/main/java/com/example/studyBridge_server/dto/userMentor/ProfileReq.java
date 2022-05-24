package com.example.studyBridge_server.dto.userMentor;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ProfileReq {

    private Object schoolImg;

    private Optional<List<Object>> certificatesImg;

    private Optional<List<String>> certificates;

    private ProfileTextReq profileTextReq;

}
