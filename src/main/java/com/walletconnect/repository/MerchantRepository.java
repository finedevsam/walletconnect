package com.walletconnect.repository;

import com.walletconnect.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
    Merchant findMerchantByUserId(String id);
}
