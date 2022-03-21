package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.Study;
import com.example.studyBridge_server.domaion.ToDo;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.type.ToDoStatus;
import com.example.studyBridge_server.dto.toDo.*;
import com.example.studyBridge_server.repository.StudyRepository;
import com.example.studyBridge_server.repository.ToDoRepository;
import com.example.studyBridge_server.repository.UserAndStudyRepository;
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
    private final UserAndStudyRepository userAndStudyRepository;
    private final StudyRepository studyRepository;

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

        List<ToDo> toDoList = new ArrayList<>();

        ToDo toDo = new ToDo();
        toDo.setStudy(study);
        toDo.setTask(assignToDoReq.getTask());
        toDo.setDueDate(assignToDoReq.getDueDate());
        toDo.setFeedBack("");
        toDo.setStatus(ToDoStatus.READY);

        Optional<List<User>> menteeList = userAndStudyRepository.findMenteeByStudyId(assignToDoReq.getStudyId());

        if (menteeList != null) {
            for (User mentee : menteeList.get()) {
                toDo.setUser(mentee);
                toDoList.add(toDo);

                menteeCnt++;
            }
        }

        toDoRepository.saveAll(toDoList);

        return AssignToDoRes.builder()
                .menteeCnt(menteeCnt)
                .studyId(study.getId())
                .build();

    }

    /**
     * 멘티가 status 변경 (ready -> progress -> done)
     */
    @Transactional
    public ChangeToDoStatusRes changeStatus(ChangeToDoStatusReq changeToDoStatusReq) throws Exception {
        ToDo toDo = toDoRepository.findById(changeToDoStatusReq.getToDoId()).get();

        if (!toDo.getUser().getId().equals(changeToDoStatusReq.getMenteeId())) {
            throw new Exception();
        }

        toDo.setStatus(ToDoStatus.valueOf(changeToDoStatusReq.getStatus()));

        ToDo savedToDo = toDoRepository.save(toDo);

        return ChangeToDoStatusRes.builder()
                .status(savedToDo.getStatus())
                .menteeId(savedToDo.getUser().getId())
                .toDoId(savedToDo.getId())
                .build();
    }

    /**
     * feedBack 작성
     */
    @Transactional
    public FeedBackToDoRes feedBack(FeedBackToDoReq feedBackToDoReq) throws Exception {
        ToDo toDo = toDoRepository.findById(feedBackToDoReq.getToDoId()).get();

        if (!toDo.getStudy().getMentorId().equals(feedBackToDoReq.getMentorId())) {
            throw new Exception();
        }

        toDo.setFeedBack(feedBackToDoReq.getFeedBack());

        ToDo savedToDo = toDoRepository.save(toDo);

        return FeedBackToDoRes.builder()
                .toDoId(savedToDo.getId())
                .mentorId(savedToDo.getStudy().getMentorId())
                .feedBack(savedToDo.getFeedBack())
                .build();
    }

    /**
     * 멘토가 done 상태인 ToDo를 Confirmed 로 상태 변경
     */
    @Transactional
    public ConfirmToDoRes confirm(ConfirmToDoReq confirmToDoReq) throws Exception {
        ToDo toDo = toDoRepository.findById(confirmToDoReq.getToDoId()).get();

        if (!toDo.getStatus().equals(ToDoStatus.DONE)) {
            throw new Exception();
        }

        if (!toDo.getStudy().getMentorId().equals(confirmToDoReq.getMentorId())) {
            throw new Exception();
        }

        toDo.setStatus(ToDoStatus.CONFIRMED);

        ToDo savedToDo = toDoRepository.save(toDo);

        return ConfirmToDoRes.builder()
                .toDoId(savedToDo.getId())
                .mentorId(savedToDo.getStudy().getMentorId())
                .toDoStatus(savedToDo.getStatus())
                .build();
    }


    /**
     * 해당 스터디 멘티의 ToDoList 불러오기
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
                                .menteeId(toDo.getUser().getId())
                                .task(toDo.getTask())
                                .dueDate(toDo.getDueDate())
                                .feedBack(toDo.getFeedBack())
                                .toDoStatus(toDo.getStatus())
                                .build();

                        result.add(findToDoRes);
                    }
            );
        }

        return result;
    }

    /**
     * 멘티의 총 ToDo 개수 불러오기
     */
    public int countOfMentee(Long menteeId) {
        return toDoRepository.countDistinctByUserId(menteeId);
    }

    /**
     * 멘티 confirmed 상태인 ToDoList 불러오기
     */
    public List<FindToDoRes> findConfirmedOfMentee(Long menteeId) {
        List<FindToDoRes> result = new ArrayList<>();

        Optional<List<ToDo>> toDoList = toDoRepository.findAllByUserIdAndStatus(menteeId, ToDoStatus.CONFIRMED);

        if (toDoList != null) {
            toDoList.get().forEach(
                    toDo -> {
                        FindToDoRes findToDoRes = FindToDoRes.builder()
                                .id(toDo.getId())
                                .menteeId(toDo.getUser().getId())
                                .task(toDo.getTask())
                                .dueDate(toDo.getDueDate())
                                .feedBack(toDo.getFeedBack())
                                .toDoStatus(toDo.getStatus())
                                .build();

                        result.add(findToDoRes);
                    }
            );
        }

        return result;
    }

}
