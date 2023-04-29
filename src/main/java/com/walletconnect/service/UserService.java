package com.walletconnect.service;

import com.walletconnect.entity.User;
import com.walletconnect.entity.impl.ChangePassword;
import com.walletconnect.entity.impl.CreateUserModel;
import com.walletconnect.entity.impl.ResetPassword;
import com.walletconnect.entity.impl.ResetPasswordConfirm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

public interface UserService {

    ResponseEntity<Object> createMerchant(CreateUserModel user);

    User getLoggedInUser();

    Page<User> getAllUser(Pageable pageable);

    Optional<User> getUserById(String id);

    ResponseEntity<Object> changePassword(ChangePassword changePassword);

    ResponseEntity<Object> resetPassword(ResetPassword resetPassword);

    ResponseEntity<Object> resetPasswordConfirm(String token, ResetPasswordConfirm passwordConfirm);
}

