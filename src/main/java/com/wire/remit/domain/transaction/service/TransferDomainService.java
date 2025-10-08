package com.wire.remit.domain.transaction.service;

import com.wire.remit.domain.account.model.Account;
import com.wire.remit.domain.account.model.Money;

public class TransferDomainService {
    
    public void transfer(Account source, Account target, Money amount, Money fee) {
        source.withdraw(amount.add(fee));
        target.deposit(amount);
    }
}
