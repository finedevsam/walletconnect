package com.walletconnect.service.impl;

import com.walletconnect.entity.User;
import com.walletconnect.entity.impl.CreateUserModel;
import com.walletconnect.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User createUser(CreateUserModel user) {
        return null;
    }

    @Override
    public User getLoggedInUser() {
        return null;
    }

    @Override
    public Page<User> getAllUser(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return Optional.empty();
    }
}
