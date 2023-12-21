package com.ll.netmong.domain.hashtag.service;

import com.ll.netmong.domain.hashtag.entity.Hashtag;
import com.ll.netmong.domain.hashtag.repository.HashtagRepository;
import com.ll.netmong.domain.post.dto.request.PostRequest;
import com.ll.netmong.domain.post.entity.Post;
import com.ll.netmong.domain.postHashtag.entity.PostHashtag;
import com.ll.netmong.domain.postHashtag.repository.PostHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashtagServiceImpl implements HashtagService {
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;

    private static final Pattern CONTENT_PATTERN = Pattern.compile("#(\\S+)");

    @Override
    @Transactional
    public List<Hashtag> saveHashtag(PostRequest postRequest, Post post) {
        List<String> tags = parsingContent(postRequest.getContent());
        List<Hashtag> hashtags = new ArrayList<>();

        for (String tag : tags) {
            Hashtag hashtag = hashtagRepository.findByName(tag)
                    .orElseGet(() -> hashtagRepository.save(new Hashtag(tag)));

            hashtags.add(hashtag);

            postHashtagRepository.save(new PostHashtag(post, hashtag));
        }

        return hashtags;
    }

    public List<String> parsingContent(String content) {
        Matcher matcher = CONTENT_PATTERN.matcher(content);
        List<String> tags = new ArrayList<>();

        while (matcher.find()) {
            tags.add(matcher.group(1));
        }

        return tags;
    }
}
