package ru.flamexander.transfer.service.core.backend.integrations;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import ru.flamexander.transfer.service.core.backend.dtos.ClientInfoResponseDto;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;

import java.util.Map;

@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(RestTemplate.class)
public class ClientsInfoServiceIntegrationRestClientImpl implements ClientsInfoServiceIntegration {
    private static final Logger logger = LoggerFactory.getLogger(ClientsInfoServiceIntegrationRestClientImpl.class);
  //  private final RestClient clientsInfoClient;
    private final Map<String,RestClient> restClientFactory;

    public ClientInfoResponseDto getClientInfo(Long id) {
        restClientFactory.entrySet().stream().forEach(e-> logger.info("---getClientInfo1   service.getName() {}   restClient  = {}",e.getKey(), e.getValue().toString()));
      //  return clientsInfoClient.get()
        return restClientFactory.get("clients").get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.NOT_FOUND.value(), (request, response) -> {
                    throw new AppLogicException("RECEIVER_NOT_EXIST", "Получатель с id = " + id + " не существует");
                })
                .body(ClientInfoResponseDto.class);
    }
}
