package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domain.AssignedToDo;
import com.example.studyBridge_server.domain.type.ToDoStatus;
import com.example.studyBridge_server.dto.assignedToDo.*;
import com.example.studyBridge_server.repository.AssignedToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AssignedToDoService {

    private final AssignedToDoRepository assignedToDoRepository;
    private final UserAuthService userAuthService;

    /**
     * 멘티가 status 변경 (ready -> progress -> done)
     */
    @Transactional
    public ChangeToDoStatusRes changeStatus(ChangeToDoStatusReq changeToDoStatusReq) throws Exception {
        AssignedToDo assignedToDo = assignedToDoRepository.findById(changeToDoStatusReq.getAssignedToDoId()).get();

        if (!assignedToDo.getUser().getId().equals(changeToDoStatusReq.getMenteeId())) {
            throw new Exception();
        }

        assignedToDo.setStatus(ToDoStatus.valueOf(changeToDoStatusReq.getStatus()));

        AssignedToDo savedAssignedToDo = assignedToDoRepository.save(assignedToDo);

        return ChangeToDoStatusRes.builder()
                .status(savedAssignedToDo.getStatus())
                .menteeId(savedAssignedToDo.getUser().getId())
                .assignedToDoId(savedAssignedToDo.getId())
                .build();
    }

    /**
     * 멘토가 done 상태인 ToDo를 Confirmed 로 상태 변경
     */
    @Transactional
    public ConfirmToDoRes confirm(ConfirmToDoReq confirmToDoReq) throws Exception {
        AssignedToDo assignedToDo = assignedToDoRepository.findById(confirmToDoReq.getAssignedToDoId()).get();

        if (!assignedToDo.getStatus().equals(ToDoStatus.DONE)) {
            throw new Exception();
        }

        if (!assignedToDo.getToDo().getStudy().getMentorId().equals(confirmToDoReq.getMentorId())) {
            throw new Exception();
        }

        assignedToDo.setStatus(ToDoStatus.CONFIRMED);

        AssignedToDo savedAssignedToDo = assignedToDoRepository.save(assignedToDo);

        return ConfirmToDoRes.builder()
                .assignedToDoId(savedAssignedToDo.getId())
                .mentorId(savedAssignedToDo.getToDo().getStudy().getMentorId())
                .toDoStatus(savedAssignedToDo.getStatus())
                .build();
    }

    /**
     * 멘티의 총 ToDo 개수 불러오기
     */
    public int countByMentee(Long menteeId) {
        return assignedToDoRepository.countDistinctByUserId(menteeId);
    }

    /**
     * 멘티의 특정 상태인 ToDo 개수 불러오기
     */
    public int countByMenteeAndStatus(Long menteeId, ToDoStatus status) {
        return assignedToDoRepository.countDistinctByUserIdAndStatus(menteeId, status);
    }

    /**
     * 멘티 특정 상태인 ToDoList 불러오기
     */
    public List<FindAssignedToDoRes> findByMenteeAndStatus(Long menteeId, ToDoStatus status) {
        Optional<List<AssignedToDo>> assignedToDoList = assignedToDoRepository.findAllByUserIdAndStatus(menteeId, status);

        return getFindAssignedToDoRes(assignedToDoList);
    }

    /**
     * ToDoId 로 해당 ToDo를 부여받은 멘티 목록 불러오기
     */
    public List<FindAssignedToDoRes> findByToDo(Long toDoId) {

        Optional<List<AssignedToDo>> assignedToDoList = assignedToDoRepository.findAllByToDoId(toDoId);

        return getFindAssignedToDoRes(assignedToDoList);
    }

    /**
     * 멘티 아이디로 ToDoList 불러오기
     */
    public List<FindAssignedToDoRes> findByMentee(Long menteeId) {

        Optional<List<AssignedToDo>> assignedToDoList = assignedToDoRepository.findAllByUserId(menteeId);

        return getFindAssignedToDoRes(assignedToDoList);
    }

    /**
     * assignedToDoList를 List<FindAssignedToDoRes> 로 변환
     */
    private List<FindAssignedToDoRes> getFindAssignedToDoRes(Optional<List<AssignedToDo>> assignedToDoList) {
        List<FindAssignedToDoRes> result = new ArrayList<>();

        if (assignedToDoList.isPresent()) {
            assignedToDoList.get().forEach(
                    assignedToDo -> {

                        FindAssignedToDoRes findAssignedToDoRes = FindAssignedToDoRes.builder()
                                .id(assignedToDo.getId())
                                .toDoId(assignedToDo.getToDo().getId())
                                .studyId(assignedToDo.getToDo().getStudy().getId())
                                .menteeId(assignedToDo.getUser().getId())
                                .menteeName(assignedToDo.getUser().getName())
                                .menteeProfileImg(assignedToDo.getUser().getProfileImg())
                                .mentorName(userAuthService.getName((assignedToDo.getToDo().getStudy().getMentorId())))
                                .mentorProfileImg(userAuthService.getProfileImg((assignedToDo.getToDo().getStudy().getMentorId())))
                                .task(assignedToDo.getToDo().getTask())
                                .explain(assignedToDo.getToDo().getToDoExplain())
                                .dueDate(assignedToDo.getToDo().getDueDate())
                                .status(assignedToDo.getStatus().toString())
                                .build();

                        result.add(findAssignedToDoRes);
                    }
            );
        }

        return result;
    }

    public Map<String, Integer> countByMenteeAndStudy(Long menteeId, Long studyId) {
        Map<String, Integer> result = new HashMap<>();

        result.put("total", assignedToDoRepository.countDistinctByUserIdAndToDo_StudyId(menteeId, studyId));
        result.put("confirmed", assignedToDoRepository.countDistinctByUserIdAndToDo_StudyIdAndStatus(menteeId, studyId, ToDoStatus.CONFIRMED));

        return result;
    }
}
