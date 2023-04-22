package com.walletconnect.entity;

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
@Table(name = "tbl_wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double balance = 0.00;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @Column(name = "merchant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Merchant merchant;
}
