package ru.flamexander.transfer.service.core.backend.configurations;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor
@Data
public class ServicePropertyOne {
    private String name;
    private String url;
    private Duration readTimeout;
    private Duration writeTimeout;
}
//
//integrations2:
//    clients-info-service:
//        name: "clients"
//        url: "http://localhost:8190/api/v1/clients"
//        read-timeout: 30s
//        write-timeout: 2s
//   limits-service:
//        name: "limits"
//        url: "http://localhost:8191/api/v1/limits"
//        read-timeout: 20s
//        write-timeout: 1s