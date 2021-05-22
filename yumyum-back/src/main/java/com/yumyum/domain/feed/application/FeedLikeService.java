package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedFindDao;
import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.dao.LikeFindDao;
import com.yumyum.domain.feed.dto.LikeFeedRequest;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.feed.exception.FeedNotCompletedException;
import com.yumyum.domain.feed.exception.LikeDuplicateException;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedLikeService {

    private final UserFindDao userFindDao;
    private final FeedFindDao feedFindDao;
    private final LikeDao likeDao;
    private final LikeFindDao likeFindDao;

    public void doLikeFeed(final LikeFeedRequest dto){
        final Feed feed = feedFindDao.findById(dto.getFeedId());
        final User user = userFindDao.findById(dto.getUserId());

        if(likeDao.existsByFeedIdAndUserId(feed.getId(), user.getId())){
            throw new LikeDuplicateException();
        }else if(!feed.getIsCompleted()){
            throw new FeedNotCompletedException();
        }else{
            likeDao.save(dto.toEntity(feed, user));
        }
    }

    public void doCancelLikeFeed(final Long feedId, final Long userId){
        final Like like = likeFindDao.findByFeedIdAndUserId(feedId, userId);
        likeDao.delete(like);
    }
}
