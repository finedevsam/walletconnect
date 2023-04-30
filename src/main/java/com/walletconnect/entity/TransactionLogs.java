package com.walletconnect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_transaction_logs")
public class TransactionLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String sender;

    private String receiver;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "receiver_id")
    private String receiverId;

    @Column(name = "transaction_ref")
    private String transactionRef;

    @Column(name = "transaction_type")
    private String transactionType;

    private Double amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
}
