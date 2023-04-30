package com.walletconnect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_merchant")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "merchant_name")
    private String merchantName;

    private String address;

    @Column(name = "business_no")
    private String businessNo;

    @Column(name = "business_email")
    private String businessEmail;

    @Column(name = "business_reg_no")
    private String businessRegNo;

    @Column(name = "business_reg_cert")
    private String businessRegCert;

    @Column(name = "secret_key")
    private String secretKey;

    private String code;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_person_mobile")
    private String contactPersonNo;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "is_activate")
    private Boolean isActivate = false;

    @Column(name = "payment_tag")
    private String paymentTag;

    @Column(name = "has_payment_tag")
    private Boolean hasPaymentTag = false;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToOne(mappedBy = "merchant", optional = false)
    private Wallet wallet;
}
