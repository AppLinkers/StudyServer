package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.*;
import com.example.studyBridge_server.domaion.type.Role;
import com.example.studyBridge_server.domaion.type.StudyStatus;
import com.example.studyBridge_server.dto.study.*;
import com.example.studyBridge_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final UserAndStudyRepository userAndStudyRepository;
    private final RoomRepository roomRepository;
    private final UserAndRoomRepository userAndRoomRepository;

    public StudyMakeRes make(StudyMakeReq studyMakeReq) {
        User user = userRepository.findUserByLoginId(studyMakeReq.getMakerId()).get();
        Study study = Study.builder()
                .makerId(user.getId())
                .name(studyMakeReq.getName())
                .info(studyMakeReq.getInfo())
                .place(studyMakeReq.getPlace())
                .maxNum(studyMakeReq.getMaxNum())
                .type(studyMakeReq.getType())
                .status(StudyStatus.APPLY)
                .build();

        // 멘토가 스터디 제작 시
        if (user.getRole().equals(Role.MENTOR)) {
            study.setMentorId(user.getId());
        }


        Study savedStudy = studyRepository.save(study);

        //  스터디 생성 시, 채팅방 생성 - 입장 메세지는 실제 채팅창 화면에 들어갔을때 날리는 것으로 한다.
        Room room = roomRepository.save(new Room(savedStudy));

        if (user.getRole().equals(Role.MENTEE)) {
            StudyApplyReq studyApplyReq = StudyApplyReq.builder()
                    .studyId(savedStudy.getId())
                    .userId(user.getLoginId())
                    .build();

            apply(studyApplyReq);
        }

        // 멘토가 스터디 제작 시,
        if (user.getRole().equals(Role.MENTOR)) {
            // 채팅방에 해당  사용자 추가
            UserAndRoom userAndRoom = new UserAndRoom(user, room);
            userAndRoomRepository.save(userAndRoom);
        }

        StudyMakeRes studyMakeRes = StudyMakeRes.builder()
                .makerId(user.getLoginId())
                .name(savedStudy.getName())
                .build();

        return studyMakeRes;
    }

    public StudyApplyRes apply(StudyApplyReq studyApplyReq) {
        User user = userRepository.findUserByLoginId(studyApplyReq.getUserId()).get();
        Study study = studyRepository.findById(studyApplyReq.getStudyId()).get();

        UserAndStudy userAndStudy = UserAndStudy.builder()
                .study(study)
                .user(user)
                .role(user.getRole()).build();

        userAndStudyRepository.save(userAndStudy);

        // 만약 신청자가 mentee 일때, 채팅방에 입장하도록 한다. - 입장 메세지는 실제 채팅창 화면에 들어갔을때 날리는 것으로 한다.
        if (user.getRole().equals(Role.MENTEE)) {
            UserAndRoom userAndRoom = new UserAndRoom(user, roomRepository.findRoomByStudyId(study.getId()));
            userAndRoomRepository.save(userAndRoom);
        }

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

    // 해당 Study가 사용자가 신청한 스터디 인지 확인
    public Boolean isAppliedByUser(IsAppliedReq isAppliedReq) {

        if (userAndStudyRepository.findByUserAndStudy(isAppliedReq.getUserLoginId(), isAppliedReq.getStudyId()).isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> findMentorLoginIdByStudyId(Long studyId) {

        Optional<List<String>> result = userAndStudyRepository.findUserLoginIdByStudyId(studyId, "MENTOR");

        if (result.isPresent()) {
            return result.get();
        } else {
            return new ArrayList<>();
        }
    }

    public List<String> findMenteeLoginIdByStudyId(Long studyId) {
        Optional<List<String>> result = userAndStudyRepository.findUserLoginIdByStudyId(studyId, "MENTEE");

        if (result.isPresent()) {
            return result.get();
        } else {
            return new ArrayList<>();
        }
    }

    public String findMakerLoginIdByStudyId(Long studyId) {
        return studyRepository.findMakerLoginIdByStudyId(studyId);
    }

    public String changeStatus(ChangeStatusReq changeStatusReq ) {
        Study study = studyRepository.findById(changeStatusReq.getStudyId()).get();

        study.setStatus(StudyStatus.valueOf(changeStatusReq.getStatus()));

        studyRepository.save(study);

        return study.getStatus().toString();
    }

    public String getStatus(Long studyId) {
        return studyRepository.findStatus(studyId);
    }

    @Transactional
    public int deleteMentor(Long studyId) {
        // 채팅방에서 해당 사용자(멘토) 삭제
        userAndRoomRepository.deleteUserAndRoomByUserId(studyRepository.findMentorIdByStudyId(studyId));

        return studyRepository.deleteMentor(studyId);
    }
}