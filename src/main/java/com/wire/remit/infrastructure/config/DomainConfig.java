package com.wire.remit.infrastructure.config;

import com.wire.remit.domain.account.service.DailyLimitPolicy;
import com.wire.remit.domain.account.service.FeePolicy;
import com.wire.remit.domain.transaction.service.TransferDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public DailyLimitPolicy dailyLimitPolicy() {
        return new DailyLimitPolicy();
    }

    @Bean
    public FeePolicy feePolicy() {
        return new FeePolicy();
    }

    @Bean
    public TransferDomainService transferDomainService() {
        return new TransferDomainService();
    }
}
