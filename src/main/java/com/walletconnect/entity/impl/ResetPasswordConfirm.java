package com.walletconnect.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordConfirm {
    @NotNull(message = "please enter new password")
    private String newPassword;

    @NotNull(message = "Please confirm new password")
    private String confirmNewPassword;
}
