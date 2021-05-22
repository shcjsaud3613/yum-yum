package com.yumyum.domain.user.dao;

import com.yumyum.domain.user.entity.Follow;
import com.yumyum.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowDao extends JpaRepository<Follow, Long> {

    List<Follow> findByHost(User host);

    List<Follow> findByFollower(User follower);

    Optional<Follow> findByHostAndFollower(User host, User follower);
}
