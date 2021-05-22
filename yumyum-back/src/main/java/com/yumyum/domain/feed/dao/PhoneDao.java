package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneDao extends JpaRepository<Phone, Long> {

    boolean existsByPhoneHeader(String phoneHeader);
}
