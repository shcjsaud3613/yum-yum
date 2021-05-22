package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeDao extends JpaRepository<Like, Long> {

    Optional<Like> findByFeedIdAndUserId(Long feedId, Long userId);

    Optional<Like> findByFeedAndUser(Feed feed, User user);

    Boolean existsByFeedIdAndUserId(Long feedId, Long userId);

    Long countByFeedId(Long feedId);

    List<Like> findByUserId(Long userId);

    List<Like> findByFeedId(Long feedId);
}