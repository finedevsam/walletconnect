package com.walletconnect.controller;

import com.walletconnect.entity.impl.WalletTransfer;
import com.walletconnect.service.impl.WalletOperationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("wallet")
public class WalletController {

    @Autowired
    private WalletOperationServiceImpl walletOperationService;

    @PostMapping("/send")
    public ResponseEntity<Object> sendFund(@RequestBody WalletTransfer transfer){
        return walletOperationService.walletTransfer(transfer);
    }

    @GetMapping("/logs")
    public List<?> myLogs(Pageable pageable){
        return walletOperationService.myTransactionHistory(pageable);
    }
}
