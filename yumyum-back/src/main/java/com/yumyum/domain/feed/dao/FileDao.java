package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDao extends JpaRepository<File, Long> {
}
