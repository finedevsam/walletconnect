package com.walletconnect.service;

import com.walletconnect.entity.User;
import com.walletconnect.entity.impl.CreateUserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    User createUser(CreateUserModel user);

    User getLoggedInUser();

    Page<User> getAllUser(Pageable pageable);

    Optional<User> getUserById(String id);
}

