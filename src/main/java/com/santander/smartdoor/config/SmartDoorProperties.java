package com.santander.smartdoor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties
public class SmartDoorProperties {
    private String clientId;
    private String configUri;
    private String jwk;
    private String redirectUri;
    private String iotDeviceUri;
}