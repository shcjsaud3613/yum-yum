package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.dto.FeedResponse;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.map.dao.PlaceFindDao;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedResponseService {

    private final UserFindDao userFindDao;
    private final PlaceFindDao placeFindDao;
    private final LikeDao likeDao;

    public FeedResponse entityToDto(final Feed feed, final Long userId){
        final User user = userFindDao.findById(feed.getUser().getId());
        final Long likeCount = getLikeCount(feed.getId());
        final Boolean isLike = checkIsLike(feed.getId(), userId);

        if(feed.getPlace() != null) {
            final Place place = placeFindDao.findById(feed.getPlace().getId());
            return new FeedResponse(feed, user, place, likeCount, isLike);
        }else return new FeedResponse(feed, user, null, likeCount, isLike);
    }

    public FeedResponse entityToDto(final Feed feed, final Boolean isLike){
        final User user = userFindDao.findById(feed.getUser().getId());
        final Place place = placeFindDao.findById(feed.getPlace().getId());
        final Long likeCount = getLikeCount(feed.getId());
        return new FeedResponse(feed, user, place, likeCount, isLike);
    }

    public List<FeedResponse> entityToDto(final List<Feed> fList, final Long userId) {
        final List<FeedResponse> list = new ArrayList<>();
        for (Feed feed : fList) {
            list.add(entityToDto(feed, userId));
        }
        return list;
    }

    public List<FeedResponse> entityToDto(final List<Like> lList) {
        final List<FeedResponse> list = new ArrayList<>();
        for(Like like : lList){
            Feed feed = like.getFeed();
            list.add(entityToDto(feed, true));
        }
        return list;
    }

    public Long getLikeCount(final Long feedId){
        final Long likeCount = likeDao.countByFeedId(feedId);
        return likeCount;
    }

    public boolean checkIsLike(final Long feedId, final Long userId) {
        final Optional<Like> like = likeDao.findByFeedIdAndUserId(feedId, userId);
        return like.isPresent();
    }
}
