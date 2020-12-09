package com.santander.smartdoor.service;

import com.santander.digital.verifiedid.impl.VerifiedIdClientImp;
import com.santander.digital.verifiedid.model.TokenRequest;
import com.santander.digital.verifiedid.model.claims.sharing.Claims;
import com.santander.digital.verifiedid.model.init.authorize.InitiateAuthorizeRequest;
import com.santander.digital.verifiedid.model.init.authorize.InitiateAuthorizeResponse;
import com.santander.digital.verifiedid.model.token.IdToken;
import com.santander.smartdoor.config.SmartDoorProperties;
import com.santander.smartdoor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmartDoorService {

    private final VerifiedIdClientImp verifiedIdClient;
    private final UserRepository userRepository;
    private final SmartDoorProperties properties;
    private final IoTDeviceService ioTDeviceService;

    public String authorize(){

        final Claims idClaims = new Claims();
        idClaims.passportId().withEssential(true).withPurpose("To give you access to the apartment we need to verify your passport");

        InitiateAuthorizeRequest request = InitiateAuthorizeRequest.builder()
                .redirectUri(properties.getRedirectUri())
                .claims(idClaims)
                .purpose("To give you access to the apartment we need to verify your passport")
                .build();

        InitiateAuthorizeResponse initiateAuthorizeResponse = verifiedIdClient.initiateAuthorize(request);
        return  initiateAuthorizeResponse.getRedirectionUri();
    }

    public ResponseEntity<String> verify(String code) throws IoTDeviceNotAccesible {
        TokenRequest tokenRequest = TokenRequest.builder()
                .redirectUri(properties.getRedirectUri())
                .authorizationCode(code)
                .build();

        IdToken token = verifiedIdClient.token(tokenRequest);

        if(!userRepository.containsKey(token.getPassportId())){
            ioTDeviceService.accessDenied();
            return HtmlUtil.htmlError();
        }

        String name = userRepository.get(token.getPassportId());
        ioTDeviceService.openDoor(name, true);


        return HtmlUtil.htmlAccessGranted(name);
    }

}
