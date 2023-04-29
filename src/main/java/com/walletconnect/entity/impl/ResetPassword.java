package com.walletconnect.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPassword {

    @NotNull(message = "Please enter password")
    private String email;
}
