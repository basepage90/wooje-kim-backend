package com.wire.remit.adapter.out.persistence.entity;

import com.wire.remit.domain.transaction.model.TransactionType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

/**
 * 거래 내역
 */
@Entity
@Table(name = "transaction")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity accountEntity;

    /**
     * 거래 타입
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    /**
     * 거래 금액
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * 거래 발생일시
     */
    @Column(nullable = false)
    private LocalDateTime occurredAt;

    @Builder
    private TransactionEntity(Long id, AccountEntity accountEntity, TransactionType type, BigDecimal amount, LocalDateTime occurredAt) {
        this.id = id;
        this.accountEntity = accountEntity;
        this.type = type;
        this.amount = amount;
        this.occurredAt = occurredAt;
    }
}
