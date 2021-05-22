package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.dto.UpdateRequest;
import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserUpdateService {

    private final UserFindDao userFindDao;
    private final RegexChecker regexChecker;

    public UserResponse updateUser(final UpdateRequest dto){
        final User user = userFindDao.findById(dto.getId());

        // 이메일, 별명, 패스워드 비어있는지 확인
        regexChecker.stringCheck("Nickname", dto.getNickname());
        regexChecker.stringCheck("Introduction", dto.getIntroduction());

        // 프로필 사진이 등록되지 않았을 경우 빈 문자열 저장
        if(dto.getProfilePath() == null){
            dto.setProfilePath("");
        }

        user.updateUser(dto);
        return new UserResponse(user);
    }
}
