package com.wire.remit.application.service;

import com.wire.remit.application.dto.command.RegisterAccountCommand;
import com.wire.remit.application.dto.result.AccountResult;
import com.wire.remit.application.mapper.AccountAppMapper;
import com.wire.remit.application.port.out.LoadAccountPort;
import com.wire.remit.application.port.out.SaveAccountPort;
import com.wire.remit.domain.account.model.Account;
import com.wire.remit.domain.account.model.AccountStatus;
import com.wire.remit.domain.common.DomainException;
import com.wire.remit.domain.common.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountService 단위 테스트")
class AccountServiceTest {

    @Mock
    private LoadAccountPort loadAccountPort;

    @Mock
    private SaveAccountPort saveAccountPort;

    @Mock
    private AccountAppMapper mapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("계좌 등록 성공")
    void register_success() {
        // given
        RegisterAccountCommand command = RegisterAccountCommand.builder()
                .ownerName("김큰손").accountName("사업통장").build();

        Account registerAccount = Account.builder()
                .ownerName("김큰손").accountName("사업통장").status(AccountStatus.ACTIVE).build();

        Account savedAccount = Account.builder()
                .id(1L).ownerName("김큰손").accountName("사업통장").status(AccountStatus.ACTIVE).build();

        AccountResult expectedResult = AccountResult.builder()
                .id(1L).ownerName("김큰손").accountName("사업통장").status(AccountStatus.ACTIVE).build();

        given(mapper.toRegisterDomain(command)).willReturn(registerAccount);
        given(saveAccountPort.save(registerAccount)).willReturn(savedAccount);
        given(mapper.toAccountResult(savedAccount)).willReturn(expectedResult);

        // when
        AccountResult result = accountService.register(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);

        then(mapper).should(times(1)).toRegisterDomain(command);
        then(saveAccountPort).should(times(1)).save(registerAccount);
        then(mapper).should(times(1)).toAccountResult(savedAccount);
    }

    @Test
    @DisplayName("계좌 삭제 성공")
    void delete_success() {
        // given
        Long accountId = 1L;
        Account activeAccount = Account.builder()
                .id(accountId)
                .ownerName("김큰손")
                .accountName("사업통장")
                .status(AccountStatus.ACTIVE)
                .build();

        Account closedAccount = Account.builder()
                .id(accountId)
                .ownerName("김큰손")
                .accountName("사업통장")
                .status(AccountStatus.CLOSED)
                .build();

        given(loadAccountPort.findById(accountId)).willReturn(Optional.of(activeAccount));
        given(saveAccountPort.save(activeAccount)).willReturn(closedAccount);

        // when
        accountService.delete(accountId);

        // then
        then(loadAccountPort).should(times(1)).findById(accountId);
        then(saveAccountPort).should(times(1)).save(activeAccount);
    }

    @Test
    @DisplayName("계좌 삭제 실패 - 계좌 없음")
    void delete_fail_notFound() {
        // given
        Long accountId = 99L;
        given(loadAccountPort.findById(accountId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> accountService.delete(accountId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());

        then(loadAccountPort).should(times(1)).findById(accountId);
        then(saveAccountPort).shouldHaveNoInteractions();
    }
}
