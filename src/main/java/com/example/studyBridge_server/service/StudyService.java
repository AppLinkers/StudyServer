package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.Study;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.UserAndStudy;
import com.example.studyBridge_server.domaion.type.StudyStatus;
import com.example.studyBridge_server.dto.study.*;
import com.example.studyBridge_server.repository.StudyRepository;
import com.example.studyBridge_server.repository.UserAndStudyRepository;
import com.example.studyBridge_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final UserAndStudyRepository userAndStudyRepository;

    public StudyMakeRes make(StudyMakeReq studyMakeReq) {
        User user = userRepository.findUserByLoginId(studyMakeReq.getMakerId()).get();
        Study study = new Study();
        study.setMakerId(user.getId());
        study.setName(studyMakeReq.getName());
        study.setInfo(studyMakeReq.getInfo());
        study.setPlace(studyMakeReq.getPlace());
        study.setMaxNum(studyMakeReq.getMaxNum());
        study.setType(studyMakeReq.getType());
        study.setStatus(StudyStatus.APPLY);

        Study savedStudy = studyRepository.save(study);

        StudyApplyReq studyApplyReq = StudyApplyReq.builder()
                .studyId(savedStudy.getId())
                .userId(user.getLoginId())
                .build();

        apply(studyApplyReq);

        StudyMakeRes studyMakeRes = StudyMakeRes.builder()
                .makerId(user.getLoginId())
                .name(savedStudy.getName())
                .build();

        return studyMakeRes;
    }

    public StudyApplyRes apply(StudyApplyReq studyApplyReq) {
        User user = userRepository.findUserByLoginId(studyApplyReq.getUserId()).get();
        Study study = studyRepository.findById(studyApplyReq.getStudyId()).get();

        UserAndStudy userAndStudy = new UserAndStudy();
        userAndStudy.setStudy(study);
        userAndStudy.setUser(user);
        userAndStudy.setRole(user.getRole());

        user.addUserAndStudy(userAndStudy);
        study.addUserAndStudy(userAndStudy);

        userAndStudyRepository.save(userAndStudy);
        userRepository.save(user);
        studyRepository.save(study);

        StudyApplyRes studyApplyRes = StudyApplyRes.builder()
                .studyName(study.getName())
                .userName(user.getName())
                .userType(user.getRole().toString())
                .build();

        return studyApplyRes;
    }

    public List<StudyFindRes> find() {
        List<StudyFindRes> result = new ArrayList<>();
        studyRepository.findAll().forEach(study -> {
            result.add(
                    StudyFindRes.builder()
                            .id(study.getId())
                            .name(study.getName())
                            .info(study.getInfo())
                            .maxNum(study.getMaxNum())
                            .status(study.getStatus().toString())
                            .type(study.getType())
                            .place(study.getPlace())
                            .build()
            );
        });

        return result;
    }
}
