package ru.flamexander.transfer.service.core.backend.configurations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties({
        AppProperties.class,
        ClientsInfoServiceProperties.class,
        LimitsServiceProperties.class
})
public class AppConfig {
  //  LimitsServiceProperties limitsServiceProperties;
    //    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

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
    }
    private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(LimitsServiceProperties properties) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(properties.getReadTimeout());
        clientHttpRequestFactory.setConnectionRequestTimeout(properties.getWriteTimeout());
        return clientHttpRequestFactory;
    }
}
