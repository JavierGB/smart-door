package com.santander.smartdoor.service;

import com.santander.smartdoor.config.SmartDoorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class IoTDeviceService {

    private final RestTemplate restTemplate;
    private final SmartDoorProperties properties;

    public void openDoor(final String name, final boolean state) throws IoTDeviceNotAccesible{
        try{
            ResponseEntity<String> forEntity = restTemplate.getForEntity(
                    URI.create(properties.getIotDeviceUri() + "?userName=" + name + "&validPassport=" + state),
                    String.class
            );
            log.info("forEntity: " + forEntity.getBody());
        }catch (RestClientException e){
            throw new IoTDeviceNotAccesible();
        }
    }

    public void accessDenied() throws IoTDeviceNotAccesible{
        try{
            ResponseEntity<String> forEntity = restTemplate.getForEntity(
                    URI.create(properties.getIotDeviceUri() + "?userName=notDefined" + "&validPassport=false"),
                    String.class
            );
            log.info("forEntity: " + forEntity.getBody());
        }catch (RestClientException e){
            throw new IoTDeviceNotAccesible();
        }
    }
}
