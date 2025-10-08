package com.wire.remit.adapter.out.persistence.entity;

import com.wire.remit.domain.account.model.AccountStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

/**
 * 계좌 정보
 */
@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 예금주 이름 */
    @Column(nullable = false)
    private String ownerName;

    /** 계좌 이름 */
    @Column
    private String accountName;

    /** 계좌 잔액 */
    @Column(nullable = false)
    private BigDecimal balance;

    /** 계좌 상태(ACTIVE/CLOSED) */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Version
    private Long version;

    @Builder
    private AccountEntity(Long id, String ownerName, String accountName, BigDecimal balance, AccountStatus status, Long version) {
        this.id = id;
        this.ownerName = ownerName;
        this.accountName = accountName;
        this.balance = balance == null ? BigDecimal.ZERO : balance;
        this.status = status == null ? AccountStatus.ACTIVE : status;
        this.version = version;
    }
}
