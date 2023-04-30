package com.walletconnect.repository;

import com.walletconnect.entity.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, String> {

    UserWallet findByUserId(String id);
}
