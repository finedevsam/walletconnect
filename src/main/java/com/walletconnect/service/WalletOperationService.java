package com.walletconnect.service;

import com.walletconnect.entity.impl.WalletTransfer;
import org.springframework.http.ResponseEntity;

public interface WalletOperationService {
    ResponseEntity<Object> walletTransfer(WalletTransfer transfer);
}
