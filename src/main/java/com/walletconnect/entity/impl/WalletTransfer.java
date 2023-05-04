package com.walletconnect.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WalletTransfer {

    @NotNull(message = "Please enter receiver email")
    private String paymentTag;

    @NotNull(message = "Please enter amount")
    private String amount;

    @NotNull(message = "Please enter pin")
    private String pin;
}
