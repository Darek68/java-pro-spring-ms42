package ru.flamexander.transfer.service.core.backend.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import ru.flamexander.transfer.service.core.backend.dtos.LimitInfoDto;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;

import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(RestTemplate.class)
public class LimitsInfoServiceIntegrationRestClientImpl implements LimitsInfoServiceIntegration {
    private final RestClient limitsInfoClient;

    @Override
    public LimitInfoDto getLimitInfo(Long id) {
        return limitsInfoClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.NOT_FOUND.value(), (request, response) -> {
                    throw new AppLogicException("RECEIVER_NOT_EXIST", "Лимит получателя с id = " + id + " не существует");
                })
                .body(LimitInfoDto.class);
    }

    @Override
    public LimitInfoDto setLimitInfo(Long id, BigDecimal amt) {
        return limitsInfoClient.post()
                .uri("/set", id)
                .contentType(APPLICATION_JSON)
                .body(new LimitInfoDto(id,amt))
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.NOT_FOUND.value(), (request, response) -> {
                    throw new AppLogicException("ERROR_IN_LIMIT_SERVICE", "Не удалось установить лимит получателя с id = " + id);
                })
                .body(LimitInfoDto.class);

    }
}
