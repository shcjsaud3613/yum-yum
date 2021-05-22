package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.exception.FeedNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedFindDao {

    private final FeedDao feedDao;

    public Feed findById(final Long id){
        final Optional<Feed> feed = feedDao.findById(id);
        feed.orElseThrow(() -> new FeedNotFoundException(id));
        return feed.get();
    }
}
