package com.walletconnect.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentTag {
    @NotNull(message = "enter payment tag")
    private String tag;
}
