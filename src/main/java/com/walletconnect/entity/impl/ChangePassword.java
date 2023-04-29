package com.walletconnect.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePassword {

    @NotNull(message = "Please enter old password")
    private String oldPassword;

    @NotNull(message = "Please enter new password")
    private String newPassword;

    @NotNull(message = "Please confirm new password")
    private String confirmNewPassword;
}
