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
    private final UserAndRoomRepository userAndRoomRepository;
    private final RoomRepository roomRepository;

    public StudyMakeRes make(StudyMakeReq studyMakeReq) {
        User user = userRepository.findUserByLoginId(studyMakeReq.getMakerId()).get();
        Study study = Study.builder()
                .makerId(user.getId())
                .name(studyMakeReq.getName())
                .info(studyMakeReq.getInfo())
                .studyExplain(studyMakeReq.getExplain())
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
            Room room = roomRepository.findRoomByStudyId(study.getId());
            UserAndRoom userAndRoom = new UserAndRoom(user, room);
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
                            .explain(study.getStudyExplain())
                            .maxNum(study.getMaxNum())
                            .status(study.getStatus().toString())
                            .type(study.getType())
                            .place(study.getPlace())
                            .menteeCnt(menteeCntOfStudy(study.getId()))
                            .build()
            );
        });

        return result;
    }

    public int menteeCntOfStudy(Long studyId) {
        return userAndStudyRepository.countAllByStudyIdAndRole(studyId, Role.MENTEE);
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

    // 구독 해제?
    @Transactional
    public int deleteMentor(Long studyId) {
        // 해당 스터디의 멘토 null 처리
        Study study = studyRepository.findById(studyId).get();
        study.setMentorId(null);
        studyRepository.save(study);

        // 채팅방에서 해당 사용자(멘토) 삭제
        Room room = roomRepository.findRoomByStudyId(studyId);
        Long mentorId = studyRepository.findMentorIdByStudyId(studyId).get();

        return userAndRoomRepository.deleteUserAndRoomByRoomIdAndUserId(room.getId(), mentorId);
    }

    @Transactional
    public ChooseMentorRes chooseMentor(Long studyId, String mentorLoginId) {
        Long mentorId = userRepository.findUserByLoginId(mentorLoginId).get().getId();
        // 지원한 멘토 목록 다 지우기
        userAndStudyRepository.chooseMentor(studyId, Role.MENTOR);

        // 해당 스터디의 mentor 지정
        Study study = studyRepository.findById(studyId).get();
        study.setMentorId(mentorId);

        studyRepository.save(study);

        // 해당 멘토를 채팅방에 초대
        Room room = roomRepository.findRoomByStudyId(studyId);
        UserAndRoom userAndRoom = new UserAndRoom(userRepository.findById(mentorId).get(), room);
        userAndRoomRepository.save(userAndRoom);

        return ChooseMentorRes.builder()
                .studyId(studyId)
                .mentorId(mentorId)
                .studyName(study.getName())
                .build();
    }

    public String findChosenMentorLoginId(Long studyId) {
        Optional<Long> mentorId = studyRepository.findMentorIdByStudyId(studyId);

        if (mentorId.isPresent()) {
            return userRepository.findLoginIdById(mentorId.get());
        } else {
            return "현재 멘토가 지정되어있지 않습니다.";
        }
    }

    /**
     *
     * 스터디 제거시
     * 해당 채팅방 데이터 삭제 : Message, UserAndRoom, Room
     * 스터디 데이터 삭제 : UserAndStudy, Study
     */
    @Transactional
    public StudyDeleteRes delete(StudyDeleteReq studyDeleteReq) {
        Study study = studyRepository.findById(studyDeleteReq.getStudyId()).get();
        if (study.getMakerId().equals(studyDeleteReq.getMakerId())) {
            roomRepository.deleteRoomByStudyId(studyDeleteReq.getStudyId());

            studyRepository.delete(study);

            return StudyDeleteRes.builder()
                    .studyId(study.getId())
                    .makerId(study.getMakerId())
                    .build();
        } else {
            return StudyDeleteRes.builder()
                    .studyId(-1L)
                    .makerId(-1L)
                    .build();
        }

    }

    /**
     * 스터디 수정 기능
     * param : 스터디 id, maker id,
     */
    @Transactional
    public StudyUpdateRes update(StudyUpdateReq studyUpdateReq) throws Exception {
        Study study = studyRepository.findById(studyUpdateReq.getStudyId()).get();

        if (!study.getMakerId().equals(studyUpdateReq.getMakerId())) {
            throw new Exception();
        }

        study.setName(studyUpdateReq.getName());
        study.setType(studyUpdateReq.getType());
        study.setInfo(studyUpdateReq.getInfo());
        study.setStudyExplain(studyUpdateReq.getExplain());
        study.setMaxNum(studyUpdateReq.getMaxNum());

        Study savedStudy = studyRepository.save(study);

        return StudyUpdateRes.builder()
                .studyId(savedStudy.getId())
                .makerId(savedStudy.getMakerId())
                .name(savedStudy.getName())
                .type(savedStudy.getType())
                .info(savedStudy.getInfo())
                .explain(savedStudy.getStudyExplain())
                .maxNum(savedStudy.getMaxNum())
                .build();
    }

    /**
     * 내가 속한 스터디 목록 반환
     */
    public List<StudyFindRes> findByUserId(Long userId) {
        List<StudyFindRes> result = new ArrayList<>();

        User user = userRepository.findById(userId).get();

        if (user.getRole().equals(Role.MENTEE)) {
            Optional<List<Study>> studyList = studyRepository.findByMenteeId(userId);
            result = convertStudyListToStudyFindResList(studyList);
        } else if (user.getRole().equals(Role.MENTOR)) {
            Optional<List<Study>> studyList = studyRepository.findByMentorId(userId);
            result = convertStudyListToStudyFindResList(studyList);
        }

        return result;
    }

    /**
     * StudyList를 List<StudyFindRes> 로 변환
     */
    public List<StudyFindRes> convertStudyListToStudyFindResList(Optional<List<Study>> studyList) {
        List<StudyFindRes> result = new ArrayList<>();

        if (studyList.isPresent()) {
            studyList.get().forEach(
                    study -> {
                        StudyFindRes studyFindRes = StudyFindRes.builder()
                                .id(study.getId())
                                .name(study.getName())
                                .info(study.getInfo())
                                .explain(study.getStudyExplain())
                                .maxNum(study.getMaxNum())
                                .status(study.getStatus().toString())
                                .place(study.getPlace())
                                .type(study.getType())
                                .menteeCnt(menteeCntOfStudy(study.getId()))
                                .build();

                        result.add(studyFindRes);
                    }
            );
        }

        return result;
    }
}