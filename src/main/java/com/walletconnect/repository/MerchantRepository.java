package com.walletconnect.repository;

import com.walletconnect.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
    Merchant findMerchantByUserId(String id);

    Optional<Merchant> findMerchantById(String id);
}
