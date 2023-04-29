package com.walletconnect.repository;

import com.walletconnect.entity.TransactionLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogsRepository extends JpaRepository<TransactionLogs, String> {
}
