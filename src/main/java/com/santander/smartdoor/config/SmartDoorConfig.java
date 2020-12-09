package com.santander.smartdoor.config;

import com.santander.digital.verifiedid.impl.VerifiedIdClientImp;
import com.santander.digital.verifiedid.impl.VerifiedIdClientImpBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SmartDoorConfig {

    @Bean
    public VerifiedIdClientImp verifiedIdClient(SmartDoorProperties properties) {
        final VerifiedIdClientImp verifiedIdClient = new VerifiedIdClientImpBuilder()
                .withWellKnownURI(properties.getConfigUri())
                .withPrivateJWKFromFile(properties.getJwk())
                .withClientId(properties.getClientId())
                .build();
        verifiedIdClient.setUpClient();
        return verifiedIdClient;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
