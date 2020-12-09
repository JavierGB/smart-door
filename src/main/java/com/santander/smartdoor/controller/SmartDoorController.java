package com.santander.smartdoor.controller;

import com.santander.smartdoor.repository.UserRepository;
import com.santander.smartdoor.service.HtmlUtil;
import com.santander.smartdoor.service.IoTDeviceNotAccesible;
import com.santander.smartdoor.service.IoTDeviceService;
import com.santander.smartdoor.service.SmartDoorService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/smart")
public class SmartDoorController {

    private final SmartDoorService smartDoorService;
    private final UserRepository userRepository;
    private final IoTDeviceService ioTDeviceService;

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.POST, path = "/client")
    public ResponseEntity<?> grantAccess(@RequestBody ClientAccess clientAccess){
        userRepository.put(clientAccess.getPassport(), clientAccess.getName());
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/door")
    public ResponseEntity<?> access(){
        return redirect(smartDoorService.authorize());
    }

    @RequestMapping(path = "/door/callback")
    public ResponseEntity<String> callback(@RequestParam(required = false) String code, @RequestParam(required = false) String error, @RequestParam(required = false) String state) {
        try {
            if(error!=null){
                log.info("User cancel auth: {}", error);
                ioTDeviceService.accessDenied();
                return HtmlUtil.htmlError();
            }
            return smartDoorService.verify(code);
        } catch (IoTDeviceNotAccesible ioTDeviceNotAccesible) {
            return HtmlUtil.htmlError();
        }
    }

    private static ResponseEntity<String> redirect(String uri){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


    @Data
    private static class ClientAccess{
        private String passport;
        private String name;
    }

}
