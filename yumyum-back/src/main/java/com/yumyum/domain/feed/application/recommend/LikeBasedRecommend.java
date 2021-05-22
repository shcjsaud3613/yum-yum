package com.yumyum.domain.feed.application.recommend;

import com.yumyum.domain.feed.application.FeedSearchService;
import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.dto.FeedResponse;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeBasedRecommend {

    private final LikeDao likeDao;
    private final FeedSearchService feedSearchService;

    public List<FeedResponse> getRecommendList(final Long userId){
        final List<FeedResponse> fList = feedSearchService.findAll(userId);
        final List<FeedResponse> list = new ArrayList<>();
        for(FeedResponse feed : fList){
            User user = feed.getUser();
            if(user.getId() == userId) continue; // 자신이 작성한 피드는 제외
            if(feed.getLikeCount() == 0) continue; // 좋아요 수가 없는 피드는 제외
            if(likeDao.existsByFeedIdAndUserId(feed.getId(), user.getId())) continue; // 이미 좋아요 한 피드는 제외
            list.add(feed);
        }

        Collections.sort(list, new LikeBasedRecommend.FeedResponseComparator());

        return list;
    }

    public class FeedResponseComparator implements Comparator<FeedResponse>{
        @Override
        public int compare(FeedResponse a, FeedResponse b){
            if(a.getLikeCount() > b.getLikeCount()) return 1;
            else if(a.getLikeCount() < b.getLikeCount()) return -1;
            else return 0;
        }
    }
}
