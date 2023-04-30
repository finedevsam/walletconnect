package com.walletconnect.controller;

import com.walletconnect.entity.impl.WalletTransfer;
import com.walletconnect.service.impl.WalletOperationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wallet")
public class WalletController {

    @Autowired
    private WalletOperationServiceImpl walletOperationService;

    @PostMapping("/send")
    public ResponseEntity<Object> sendFund(@RequestBody WalletTransfer transfer){
        return walletOperationService.walletTransfer(transfer);
    }
}
