package com.yumyum.domain.user.dao;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDeleteDao {

    private final UserDao userDao;
    private final FeedDao feedDao;
    private final LikeDao likeDao;
    private final UserFindDao userFindDao;

    public void deleteById(final Long id){
        final List<Like> lList = likeDao.findByUserId(id);
        for(Like like : lList){
            likeDao.delete(like);
        }

        final User user = userFindDao.findById(id);

        final List<Feed> fList = feedDao.findByUser(user);
        for(Feed feed : fList){
            feedDao.delete(feed);
        }

        userDao.delete(user);
    }
}
