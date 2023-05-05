package com.walletconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_user_wallet")
public class UserWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double balance = 0.00;

    private String pin;

    @Column(name = "is_nfc")
    private Boolean isNfc = false;

    @Column(name = "nfc_token")
    private String nfcToken;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
}
