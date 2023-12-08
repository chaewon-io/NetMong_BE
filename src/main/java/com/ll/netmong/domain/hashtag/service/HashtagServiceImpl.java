package com.ll.netmong.domain.hashtag.service;

import com.ll.netmong.domain.hashtag.entity.Hashtag;
import com.ll.netmong.domain.hashtag.repository.HashtagRepository;
import com.ll.netmong.domain.post.dto.request.PostRequest;
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

    @Override
    @Transactional
    public List<Hashtag> saveHashtag(PostRequest postRequest) {
        List<String> tags = parsingContent(postRequest.getContent());
        List<Hashtag> hashtags = new ArrayList<>();

        for (String tag : tags) {
            Hashtag hashtag = hashtagRepository.findByName(tag)
                    .orElseGet(() -> hashtagRepository.save(new Hashtag(tag)));

            hashtags.add(hashtag);
        }

        return hashtags;
    }

    public List<String> parsingContent(String content) {
        Pattern pattern = Pattern.compile("#(\\S+)");
        Matcher matcher = pattern.matcher(content);
        List<String> tags = new ArrayList<>();

        while (matcher.find()) {
            tags.add(matcher.group(1));
        }

        return tags;
    }
}
