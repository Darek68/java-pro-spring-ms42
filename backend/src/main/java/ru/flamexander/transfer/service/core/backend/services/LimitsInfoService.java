package ru.flamexander.transfer.service.core.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.flamexander.transfer.service.core.backend.dtos.LimitInfoDto;
import ru.flamexander.transfer.service.core.backend.integrations.LimitsInfoServiceIntegration;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LimitsInfoService {
    private static final Logger logger = LoggerFactory.getLogger(LimitsInfoService.class.getName());
    private final LimitsInfoServiceIntegration limitsInfoServiceIntegration;

    public LimitInfoDto getLimitInfo(Long id) { return limitsInfoServiceIntegration.getLimitInfo(id); }

    public boolean isClientLimitOk(Long id, BigDecimal amt) {
        LimitInfoDto clientLimit = getLimitInfo(id);
        logger.info("isClientLimitOk1 clientId id = {}     clientLimit {}     amt {}", id, clientLimit.getLimit(), amt);
        return clientLimit.getLimit().compareTo(amt) >= 0;
    }

    public BigDecimal setLimitBalance(Long id, BigDecimal amt){return limitsInfoServiceIntegration.setLimitInfo(id, amt).getLimit();}
}
