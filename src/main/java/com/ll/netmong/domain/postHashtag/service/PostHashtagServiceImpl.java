package com.ll.netmong.domain.postHashtag.service;

import com.ll.netmong.domain.postHashtag.entity.PostHashtag;
import com.ll.netmong.domain.postHashtag.repository.PostHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostHashtagServiceImpl implements PostHashtagService {
    private final PostHashtagRepository postHashtagRepository;

    @Override
    @Transactional
    public void deleteHashtag(Long postId) {
        List<PostHashtag> postHashtags = postHashtagRepository.findByPostId(postId);

        for (PostHashtag postHashtag : postHashtags) {
            postHashtagRepository.delete(postHashtag);
        }
    }
}
