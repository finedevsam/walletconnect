package com.walletconnect.entity.impl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserModel {

    @NotNull(message = "email can not be blank")
    private String email;

    @NotNull(message = "password can not be blank")
    private String password;

    @NotNull(message = "first name is required")
    private String firstName;

    @NotNull(message = "last name is required")
    private String lastName;

    @NotNull(message = "Please enter account type")
    private String accountType;

}
