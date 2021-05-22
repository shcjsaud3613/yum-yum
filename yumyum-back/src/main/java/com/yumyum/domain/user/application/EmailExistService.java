package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.global.common.response.Existence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailExistService {

    private final UserDao userDao;
    private final RegexChecker regexChecker;

    public Existence checkEmailExist(final String email){
        regexChecker.emailCheck(email);
        final Boolean isExists = userDao.existsByEmail(email);
        return new Existence(isExists);
    }
}
