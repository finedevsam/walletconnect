package com.walletconnect.service;

import com.walletconnect.entity.User;
import com.walletconnect.entity.impl.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

public interface UserService {

    ResponseEntity<Object> userRegistration(CreateUserModel user);

    User getLoggedInUser();

    Page<User> getAllUser(Pageable pageable);

    Optional<User> getUserById(String id);

    ResponseEntity<Object> changePassword(ChangePassword changePassword);

    ResponseEntity<Object> resetPassword(ResetPassword resetPassword);

    ResponseEntity<Object> resetPasswordConfirm(String token, ResetPasswordConfirm passwordConfirm);

    ResponseEntity<?> createPaymentTag(PaymentTag paymentTag);
}

