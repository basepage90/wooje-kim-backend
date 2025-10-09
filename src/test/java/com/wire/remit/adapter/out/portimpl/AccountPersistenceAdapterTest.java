package com.wire.remit.adapter.out.portimpl;

import com.wire.remit.adapter.out.persistence.entity.AccountEntity;
import com.wire.remit.adapter.out.persistence.mapper.PersistenceMapper;
import com.wire.remit.adapter.out.persistence.repository.AccountJpaRepository;
import com.wire.remit.domain.account.model.Account;
import com.wire.remit.domain.account.model.AccountStatus;
import com.wire.remit.domain.account.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("AccountPersistenceAdapter 단위 테스트")
class AccountPersistenceAdapterTest {

    @Mock
    private AccountJpaRepository jpa;

    @Mock
    private PersistenceMapper mapper;

    @InjectMocks
    private AccountPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("findById 호출 시 ACTIVE 상태의 계좌를 Domain 으로 반환한다")
    void findById_success() {
        // given
        Long accountId = 1L;
        AccountEntity entity = AccountEntity.builder()
                .id(accountId).ownerName("김큰손").accountName("사업 통장")
                .balance(BigDecimal.valueOf(1_000_000)).status(AccountStatus.ACTIVE)
                .version(0L)
                .build();


        Account domain = Account.builder()
                .id(accountId).ownerName(entity.getOwnerName())
                .accountName(entity.getAccountName()).balance(Money.of(entity.getBalance()))
                .status(entity.getStatus())
                .version(entity.getVersion())
                .build();

        when(jpa.findByIdAndStatus(accountId, AccountStatus.ACTIVE)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        // when
        Optional<Account> result = adapter.findById(accountId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(accountId);
        assertThat(result.get().getBalance().getAmount()).isEqualTo(entity.getBalance());

        verify(jpa, times(1)).findByIdAndStatus(accountId, AccountStatus.ACTIVE);
        verify(mapper, times(1)).toDomain(entity);
    }

    @Test
    @DisplayName("findById 결과가 없으면 Optional.empty 를 반환한다")
    void findById_notFound() {
        // given
        Long accountId = 99L;
        when(jpa.findByIdAndStatus(accountId, AccountStatus.ACTIVE)).thenReturn(Optional.empty());

        // when
        Optional<Account> result = adapter.findById(accountId);

        // then
        assertThat(result).isEmpty();
        verify(mapper, never()).toDomain((AccountEntity) any());
    }

    @Test
    @DisplayName("save 호출 시 Entity 로 변환하여 JPA 저장 후 Domain 으로 매핑한다")
    void save_success() {
        // given
        Long accountId = 1L;
        AccountEntity entity = AccountEntity.builder()
                .id(accountId).ownerName("김큰손").accountName("사업 통장")
                .balance(BigDecimal.valueOf(1_000_000)).status(AccountStatus.ACTIVE)
                .version(0L)
                .build();


        Account domain = Account.builder()
                .id(accountId).ownerName(entity.getOwnerName())
                .accountName(entity.getAccountName()).balance(Money.of(entity.getBalance()))
                .status(entity.getStatus())
                .version(entity.getVersion())
                .build();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        // when
        Account result = adapter.save(domain);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBalance().getAmount()).isEqualTo(entity.getBalance());
        assertThat(result.getStatus()).isEqualTo(AccountStatus.ACTIVE);

        verify(mapper, times(1)).toEntity(domain);
        verify(jpa, times(1)).save(entity);
        verify(mapper, times(1)).toDomain(entity);
    }
}
