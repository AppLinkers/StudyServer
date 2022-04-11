package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domain.*;
import com.example.studyBridge_server.domain.type.MessageType;
import com.example.studyBridge_server.domain.type.ToDoStatus;
import com.example.studyBridge_server.dto.toDo.AssignToDoReq;
import com.example.studyBridge_server.dto.toDo.AssignToDoRes;
import com.example.studyBridge_server.dto.toDo.FindToDoReq;
import com.example.studyBridge_server.dto.toDo.FindToDoRes;
import com.example.studyBridge_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private final AssignedToDoRepository assignedToDoRepository;
    private final UserAndStudyRepository userAndStudyRepository;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final RoomRepository roomRepository;

    private final MessageService messageService;

    /**
     * 멘토가 멘티에게 과제 배정
     */
    public AssignToDoRes assign(AssignToDoReq assignToDoReq) throws Exception {

        Integer menteeCnt = 0;

        Study study = studyRepository.findById(assignToDoReq.getStudyId()).get();

        // check Mentor
        if (!study.getMentorId().equals(assignToDoReq.getMentorId())) {
            throw new Exception();
        }

        List<AssignedToDo> assignedToDoList = new ArrayList<>();

        ToDo toDo = new ToDo();
        toDo.setStudy(study);
        toDo.setTask(assignToDoReq.getTask());
        toDo.setToDoExplain(assignToDoReq.getExplain());
        toDo.setDueDate(assignToDoReq.getDueDate());

        ToDo savedToDo = toDoRepository.save(toDo);

        Optional<List<User>> menteeList = userAndStudyRepository.findMenteeByStudyId(assignToDoReq.getStudyId());

        if (menteeList.isPresent()) {
            for (User mentee : menteeList.get()) {

                AssignedToDo assignedToDo = new AssignedToDo();
                assignedToDo.setStatus(ToDoStatus.READY);
                assignedToDo.setToDo(savedToDo);
                assignedToDo.setUser(mentee);
                assignedToDoList.add(assignedToDo);

                menteeCnt++;
            }
        }

        assignedToDoRepository.saveAll(assignedToDoList);

        Room room = roomRepository.findRoomByStudyId(study.getId());
        User mentor = userRepository.findById(study.getMentorId()).get();

        Message message = Message.builder()
                .room(room)
                .senderId(mentor.getId())
                .senderName(mentor.getName())
                .messageType(MessageType.TALK)
                .message("과제가 부여되었습니다.")
                .build();

        messageService.send(message);

        return AssignToDoRes.builder()
                .menteeCnt(menteeCnt)
                .studyId(study.getId())
                .build();

    }

    /**
     * 멘토 아이디로 현재 자신이 멘토인 스터디의 투두 목록 불러오기
     */
    public List<FindToDoRes> findToDoByMentor(Long mentorId) {
        List<FindToDoRes> result = new ArrayList<>();

        Optional<List<Long>> studyIdList = studyRepository.findAllStudyIdByMentorId(mentorId);

        if (studyIdList.isPresent()) {
            studyIdList.get().forEach(
                    studyId -> {
                        Optional<List<ToDo>> toDoList = toDoRepository.findAllByStudyId(studyId);

                        if (toDoList.isPresent()) {
                            toDoList.get().forEach(
                                    toDo -> {
                                        FindToDoRes findToDoRes = FindToDoRes.builder()
                                                .id(toDo.getId())
                                                .studyId(studyId)
                                                .task(toDo.getTask())
                                                .explain(toDo.getToDoExplain())
                                                .dueDate(toDo.getDueDate())
                                                .build();

                                        result.add(findToDoRes);
                                    }
                            );
                        }
                    }
            );
        }

        return result;
    }

    /**
     * 해당 스터디 의 ToDoList 불러오기
     * Param : Long studyId, Long mentorId
     */
    public List<FindToDoRes> findOfStudy(FindToDoReq findToDoReq) throws Exception {
        List<FindToDoRes> result = new ArrayList<>();

        Study study = studyRepository.findById(findToDoReq.getStudyId()).get();
        if (!study.getMentorId().equals(findToDoReq.getMentorId())) {
            throw new Exception();
        }

        Optional<List<ToDo>> toDoList = toDoRepository.findAllByStudyId(study.getId());

        if (toDoList.isPresent()) {
            toDoList.get().forEach(
                    toDo -> {
                        FindToDoRes findToDoRes = FindToDoRes.builder()
                                .id(toDo.getId())
                                .studyId(findToDoReq.getStudyId())
                                .task(toDo.getTask())
                                .explain(toDo.getToDoExplain())
                                .dueDate(toDo.getDueDate())
                                .build();

                        result.add(findToDoRes);
                    }
            );
        }

        return result;
    }

    /**
     * 특정 ToDo 삭제
     */
    @Transactional
    public Integer delete(Long toDoId) {
        return toDoRepository.deleteAllById(toDoId);
    }


}
