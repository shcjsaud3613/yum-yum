package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.application.recommend.CollaborativeFiltering;
import com.yumyum.domain.feed.application.recommend.LikeBasedRecommend;
import com.yumyum.domain.feed.dto.FeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedRecommendService {

    private final FeedSearchService feedSearchService;
    private final CollaborativeFiltering collaborativeFiltering;
    private final LikeBasedRecommend likeBasedRecommend;

    public List<FeedResponse> getRecommendList(final Long userId){
        // 협업 필터링 추천순
        final List<FeedResponse> aList = collaborativeFiltering.getRecommendList(userId);
        if(aList.size() > 0) return aList;

        // 좋아요 인기순
        final List<FeedResponse> bList = likeBasedRecommend.getRecommendList(userId);
        if(bList.size() > 0) return bList;

        // 모든 리스트
        final List<FeedResponse> fList = feedSearchService.findAll(userId);
        final List<FeedResponse> cList = deleteFeedWriteByUser(fList, userId);
        return cList;
    }

    public List<FeedResponse> deleteFeedWriteByUser(final List<FeedResponse> fList, final Long userId){
        final List<FeedResponse> list = new ArrayList<>();
        for(FeedResponse feed : fList){
            if(feed.getUser().getId() == userId) continue; // 본인이 작성한 피드는 제외
            list.add(feed);
        }
        return list;
    }
}
