package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.FollowDao;
import com.yumyum.domain.user.dao.FollowFindDao;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.dto.FollowRequest;
import com.yumyum.domain.user.entity.Follow;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserFollowService {

    private final UserFindDao userFindDao;
    private final FollowDao followDao;
    private final FollowFindDao followFindDao;

    public void doFollowUser(final FollowRequest dto){
        final User host = userFindDao.findById(dto.getHostId());
        final User follower = userFindDao.findById(dto.getFollowerId());
        followDao.save(dto.toEntity(host, follower));
    }

    public void doCancelFollowUser(final Long hostId, final Long followerId){
        final Follow follow = followFindDao.findByHostIdAndFollowerId(hostId, followerId);
        followDao.delete(follow);
    }
}
