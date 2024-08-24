package ru.flamexander.transfer.service.core.backend.integrations;

import ru.flamexander.transfer.service.core.backend.dtos.LimitInfoDto;

import java.math.BigDecimal;

public interface LimitsInfoServiceIntegration {
    LimitInfoDto getLimitInfo(Long id);
    LimitInfoDto setLimitInfo(Long id, BigDecimal amt);
}
