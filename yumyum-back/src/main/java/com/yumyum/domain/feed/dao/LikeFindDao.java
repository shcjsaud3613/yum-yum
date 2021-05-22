package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.feed.exception.LikeNotFoundException;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeFindDao {

    private final LikeDao likeDao;
    public final FeedFindDao feedFindDao;
    private final UserFindDao userFindDao;

    public Like findByFeedIdAndUserId(final Long feedId, final Long userId){
        final Feed feed = feedFindDao.findById(feedId);
        final User user = userFindDao.findById(userId);
        final Optional<Like> like = likeDao.findByFeedAndUser(feed, user);
        like.orElseThrow(() -> new LikeNotFoundException(feedId, userId));
        return like.get();
    }
}
