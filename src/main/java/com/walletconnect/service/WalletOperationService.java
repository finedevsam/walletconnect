package com.walletconnect.service;

import com.walletconnect.entity.TransactionLogs;
import com.walletconnect.entity.impl.WalletTransfer;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WalletOperationService {
    ResponseEntity<Object> walletTransfer(WalletTransfer transfer);
    List<TransactionLogs> myTransactionHistory(Pageable pageable);
}
