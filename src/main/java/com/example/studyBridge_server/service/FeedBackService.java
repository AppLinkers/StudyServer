package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.AssignedToDo;
import com.example.studyBridge_server.domaion.FeedBack;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.dto.feedBack.FindFeedBackRes;
import com.example.studyBridge_server.dto.feedBack.WriteFeedBackReq;
import com.example.studyBridge_server.dto.feedBack.WriteFeedBackRes;
import com.example.studyBridge_server.repository.AssignedToDoRepository;
import com.example.studyBridge_server.repository.FeedBackRepository;
import com.example.studyBridge_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final AssignedToDoRepository assignedToDoRepository;

    /**
     * FeedBack 작성
     */
    public WriteFeedBackRes write(WriteFeedBackReq writeReq) {
        User writer = userRepository.findById(writeReq.getWriterId()).get();
        AssignedToDo assignedToDo = assignedToDoRepository.findById(writeReq.getAssignedToDoId()).get();

        FeedBack feedBack = new FeedBack();

        feedBack.setAssignedToDo(assignedToDo);
        feedBack.setUser(writer);
        feedBack.setComment(writeReq.getComment());

        FeedBack savedFeedBack = feedBackRepository.save(feedBack);

        return WriteFeedBackRes.builder()
                .id(savedFeedBack.getId())
                .assignedToDoId(savedFeedBack.getAssignedToDo().getId())
                .writerId(savedFeedBack.getUser().getId())
                .comment(savedFeedBack.getComment())
                .build();
    }

    /**
     * AssignedToDoId로
     * List<FeedBack> 조회
     */
    public List<FindFeedBackRes> findByAssignedToDo(Long assignedToDoId) {
        List<FindFeedBackRes> result = new ArrayList<>();

        Optional<List<FeedBack>> feedBackList = feedBackRepository.findAllByAssignedToDoId(assignedToDoId);

        return changeListFeedBackToListFindFeedBackRes(feedBackList);
    }


    /**
     * FeedBack 삭제
     */
    @Transactional
    public Integer deleteById(Long feedBackId) {
        return feedBackRepository.deleteAllById(feedBackId);
    }

    /**
     * FeedBack 수정
     */


    /**
     * Optional<List<FeedBack>> -> List<FindFeedBackRes>
     */
    public List<FindFeedBackRes> changeListFeedBackToListFindFeedBackRes(Optional<List<FeedBack>> feedBackList) {
        List<FindFeedBackRes> result = new ArrayList<>();

        if (feedBackList.isPresent()) {
            feedBackList.get().forEach(
                    feedBack -> {
                        FindFeedBackRes findFeedBackRes = FindFeedBackRes.builder()
                                .id(feedBack.getId())
                                .assignedToDoId(feedBack.getAssignedToDo().getId())
                                .writerId(feedBack.getUser().getId())
                                .writerName(feedBack.getUser().getName())
                                .comment(feedBack.getComment())
                                .build();

                        result.add(findFeedBackRes);
                    }
            );
        }

        return result;
    }

}
