package com.ll.netmong.domain.hashtag.service;

import com.ll.netmong.domain.hashtag.entity.Hashtag;
import com.ll.netmong.domain.post.dto.request.PostRequest;

import java.util.List;

public interface HashtagService {
    List<Hashtag> saveHashtag(PostRequest postRequest);
}
