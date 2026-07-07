package com.vehiqon.integrations.nomba.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NombaProperties.class)
public class NombaConfig {
}
