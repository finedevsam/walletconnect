package com.walletconnect.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateMerchantAccount {
    @NotNull(message = "Enter Business Name")
    private String businessName;

    @NotNull(message = "Enter Business Address")
    private String businessAddress;

    @NotNull(message = "Enter Business phone number")
    private String businessPhoneNo;

    @NotNull(message = "Enter business Registration No")
    private String businessRegNo;

    @NotNull(message = "Enter contact person")
    private String contactPerson;

    @NotNull(message = "Enter contact person mobile number")
    private String contactPersonMobileNo;
}
