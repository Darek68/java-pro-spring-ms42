package ru.flamexander.transfer.service.core.backend.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import ru.flamexander.transfer.service.core.backend.services.AccountsService;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({
        AppProperties.class,
        ClientsInfoServiceProperties.class,
        LimitsServiceProperties.class,
        ServicesProperties.class
})
public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    //    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public Map<String,RestClient> restClientFactory(ServicesProperties properties) {
        logger.info("---restClientFactory1 properties = {} ", properties);
        if (properties == null) return null;
        Map<String,RestClient> restClientsMap = new HashMap<>();
        RestClient restClient;
        for (ServicePropertyOne service: properties.getServicesProperties()) {
            restClient = RestClient.builder()
                      //  .requestFactory(new HttpComponentsClientHttpRequestFactory())
                        .requestFactory(getClientHttpRequestFactory(service))
                        .baseUrl(service.getUrl())
                        .build();
            restClientsMap.put(service.getName(),restClient);
            logger.info("---restClientFactory2   service.getName() {}   restClient  = {}   ",service.getName(), restClient.toString());
        }
      //  restClientsMap.entrySet().stream().forEach(e-> System.out.println(e.getKey() + "  " + e.getValue().toString()));
        restClientsMap.entrySet().stream().forEach(e-> logger.info("---restClientFactory3   service.getName() {}   restClient: {}  url: {}",e.getKey(), e.getValue()));
        return restClientsMap;
//        properties.getParam().forEach();
//
//        return RestClient.builder()
//                .requestFactory(new HttpComponentsClientHttpRequestFactory())
//                .baseUrl(properties.getParam(). .getUrl())
//                .build();
    }
/*
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestClient clientsInfoClient(ClientsInfoServiceProperties properties) {
        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl(properties.getUrl())
//                .defaultUriVariables(Map.of("variable", "foo"))
//                .defaultHeader("My-Header", "Foo")
//                .requestInterceptor(myCustomInterceptor)
//                .requestInitializer(myCustomInitializer)
                .build();
    }
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestClient limitsInfoClient(LimitsServiceProperties properties) {
        return RestClient.builder()
               // .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .requestFactory(getClientHttpRequestFactory(properties))
                .baseUrl(properties.getUrl())
//                .defaultUriVariables(Map.of("variable", "foo"))
//                .defaultHeader("My-Header", "Foo")
//                .requestInterceptor(myCustomInterceptor)
//                .requestInitializer(myCustomInitializer)
                .build();
    } */
//    private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(LimitsServiceProperties properties) {
//        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        clientHttpRequestFactory.setConnectTimeout(properties.getReadTimeout());
//        clientHttpRequestFactory.setConnectionRequestTimeout(properties.getWriteTimeout());
//        return clientHttpRequestFactory;
//    }

    private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(ServicePropertyOne properties) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(properties.getReadTimeout());
        clientHttpRequestFactory.setConnectionRequestTimeout(properties.getWriteTimeout());
        return clientHttpRequestFactory;
    }
}
