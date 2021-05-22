package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedDeleteDao {

    private final FeedDao feedDao;
    private final LikeDao likeDao;
    private final FeedFindDao feedFindDao;

    public void deleteFeed(final Long id){
        final List<Like> list = likeDao.findByFeedId(id);
        for(Like like : list){
            likeDao.delete(like);
        }

        final Feed feed = feedFindDao.findById(id);
        feedDao.delete(feed);
    }
}
