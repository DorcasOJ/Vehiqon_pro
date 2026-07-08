package com.vehiqon.integrations.nomba.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "nomba")
public class NombaProperties {

    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String subAccountId;
    private String accountId;
    private String webhookSigningKey;
}
