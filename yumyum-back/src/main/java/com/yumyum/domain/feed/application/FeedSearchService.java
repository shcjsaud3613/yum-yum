package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dao.FeedFindDao;
import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.dto.FeedResponse;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedSearchService {

    private final FeedDao feedDao;
    private final FeedFindDao feedFindDao;
    private final LikeDao likeDao;
    private final UserFindDao userFindDao;
    private final FeedResponseService feedResponseService;

    public FeedResponse findById(final Long feedId, final Long userId){
        final Feed feed = feedFindDao.findById(feedId);
        final FeedResponse response = feedResponseService.entityToDto(feed, userId);
        return response;
    }

    public List<FeedResponse> findAll(final Long userId){
        final List<Feed> fList = feedDao.findAll();
        final List<FeedResponse> list = feedResponseService.entityToDto(fList, userId);
        return list;
    }

    public List<FeedResponse> findByTitle(final String title, final Long userId){
        final List<Feed> fList = feedDao.findByTitleContainingIgnoreCase(title);
        final List<FeedResponse> list = feedResponseService.entityToDto(fList, userId);
        return list;
    }

    public List<FeedResponse> findByAuthor(final Long authorId, final Long userId){
        final User user = userFindDao.findById(authorId);
        final List<Feed> fList = feedDao.findByUser(user);
        final List<FeedResponse> list = feedResponseService.entityToDto(fList, userId);
        return list;
    }

    public List<FeedResponse> findByLike(final Long userId){
        final List<Like> lList = likeDao.findByUserId(userId);
        final List<FeedResponse> list = feedResponseService.entityToDto(lList);
        return list;
    }

    public List<FeedResponse> findByPlace(final Long placeId, final Long userId){
        final List<Feed> fList = feedDao.findAll();
        final List<FeedResponse> list = new ArrayList<>();
        for(Feed feed : fList){
            if(feed.getPlace() == null) continue;
            if(feed.getPlace().getId() == placeId){
                list.add(feedResponseService.entityToDto(feed, userId));
            }
        }
        return list;
    }
}
