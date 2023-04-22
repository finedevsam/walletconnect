package com.walletconnect.repository;

import com.walletconnect.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    Team findTeamByUserIdAndMerchantId(String userId, String merchantId);
}
