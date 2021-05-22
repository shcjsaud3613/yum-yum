package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.FollowFindDao;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.Follow;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowSearchService {

    private final UserFindDao userFindDao;
    private final FollowFindDao followFindDao;

    public List<UserResponse> getFollowList(final Long id, final String listType){
        List<UserResponse> list = new ArrayList<>();

        switch (listType){
            case "host":
                final List<Follow> hList = followFindDao.findByFollowerId(id); // 팔로우 중인 리스트
                for(Follow follow : hList){
                    User user = userFindDao.findById(follow.getHost().getId());
                    list.add(new UserResponse(user));
                }
                break;
            case "follower":
                final List<Follow> fList = followFindDao.findByHostId(id); // 팔로워 리스트
                for(Follow follow : fList){
                    User user = userFindDao.findById(follow.getFollower().getId());
                    list.add(new UserResponse(user));
                }
                break;
            default:
                break;
        }

        return list;
    }
}
