package com.walletconnect.repository;

import com.walletconnect.entity.PasswordReset;
import com.walletconnect.entity.impl.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, String> {
    Optional<PasswordReset> findByToken(String token);
}
