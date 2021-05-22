package com.yumyum.domain.user.dao;

import com.yumyum.domain.user.entity.Follow;
import com.yumyum.domain.user.entity.User;
import com.yumyum.domain.user.exception.FollowNotFountException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowFindDao {

    private final UserFindDao userFindDao;
    private final FollowDao followDao;

    public List<Follow> findByHostId(final Long hostId){
        final User host = userFindDao.findById(hostId);
        return followDao.findByHost(host);
    }

    public List<Follow> findByFollowerId(final Long followerId){
        final User follower = userFindDao.findById(followerId);
        return followDao.findByFollower(follower);
    }

    public Follow findByHostIdAndFollowerId(final Long hostId, final Long followerId){
        final User host = userFindDao.findById(hostId);
        final User follower = userFindDao.findById(followerId);
        final Optional<Follow> follow = followDao.findByHostAndFollower(host, follower);
        follow.orElseThrow(() -> new FollowNotFountException(host.getId(), follower.getId()));
        return follow.get();
    }
}
