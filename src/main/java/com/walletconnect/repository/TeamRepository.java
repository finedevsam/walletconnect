package com.walletconnect.repository;

import com.walletconnect.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    Team findTeamByUserIdAndMerchantId(String userId, String merchantId);

    Team findTeamByMerchantId(String id);

    Optional<Team> findTeamByUserId(String userId);

    List<Team> findAllByMerchantId(String merchantId);

}
