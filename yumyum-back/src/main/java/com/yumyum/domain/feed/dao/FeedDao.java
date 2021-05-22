package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedDao extends JpaRepository<Feed, Long> {

    List<Feed> findByUser(User user);

    List<Feed> findByTitleContainingIgnoreCase(String title);
}
