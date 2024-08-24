package ru.flamexander.transfer.service.core.backend.configurations;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@NoArgsConstructor
@Data
@NotNull
@ConfigurationProperties("integrations2")
public class ServicesProperties {
    private List<ServicePropertyOne> servicesProperties;
}
