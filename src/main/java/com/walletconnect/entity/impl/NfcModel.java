package com.walletconnect.entity.impl;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class NfcModel {

    private String amount;

    private String nfcToken;
}
