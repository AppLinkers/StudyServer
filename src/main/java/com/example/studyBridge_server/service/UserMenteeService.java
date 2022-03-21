package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.LikeMentor;
import com.example.studyBridge_server.dto.userMentee.LikeMentorRes;
import com.example.studyBridge_server.repository.LikeMentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserMenteeService {

    private final LikeMentorRepository likeMentorRepository;

    public LikeMentorRes likeMentor(Long menteeId, Long mentorId) {
        LikeMentor likeMentor = LikeMentor.builder()
                .menteeId(menteeId)
                .mentorId(mentorId)
                .build();

        LikeMentor savedLikeMentor = likeMentorRepository.save(likeMentor);

        return LikeMentorRes.builder()
                .menteeId(savedLikeMentor.getMenteeId())
                .mentorId(savedLikeMentor.getMentorId())
                .build();
    }

    @Transactional
    public LikeMentorRes unLikeMentor(Long menteeId, Long mentorId) {
        likeMentorRepository.deleteLikeMentorByMenteeIdAndMentorId(menteeId, mentorId);

        return LikeMentorRes.builder()
                .menteeId(menteeId)
                .mentorId(mentorId)
                .build();
    }

    public List<LikeMentorRes> findLikedMentors(Long menteeId) {
        List<LikeMentorRes> result = new ArrayList<>();
        Optional<List<LikeMentor>> searchedList = likeMentorRepository.findLikeMentorsByMenteeId(menteeId);

        if (searchedList.isPresent()) {
            searchedList.get().forEach(
                    sl -> {
                        result.add(
                                LikeMentorRes.builder()
                                        .menteeId(sl.getMenteeId())
                                        .mentorId(sl.getMentorId())
                                        .build()
                        );
                    }
            );
        }

        return result;
    }

    public Boolean isLiked(Long menteeId, Long mentorId) {
        if (likeMentorRepository.findLikeMentorByMenteeIdAndMentorId(menteeId, mentorId).isPresent()) {
            return true;
        } else {
            return false;
        }
    }


}
