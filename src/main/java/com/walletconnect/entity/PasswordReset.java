package com.walletconnect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_password_reset")
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String token;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne()
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
}
