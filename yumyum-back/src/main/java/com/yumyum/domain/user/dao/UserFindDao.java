package com.yumyum.domain.user.dao;

import com.yumyum.domain.user.entity.User;
import com.yumyum.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserFindDao {

    private final UserDao userDao;

    public User findById(final Long id){
        final Optional<User> user = userDao.findById(id);
        user.orElseThrow(() -> new UserNotFoundException(id));
        return user.get();
    }

    public User findByEmail(final String email){
        final Optional<User> user = userDao.findByEmail(email);
        if(!user.isPresent()){
            throw new UserNotFoundException(email);
        }
        return user.get();
    }
}
