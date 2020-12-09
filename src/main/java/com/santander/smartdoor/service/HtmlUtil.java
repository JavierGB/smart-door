package com.santander.smartdoor.service;

import org.springframework.http.ResponseEntity;

public class HtmlUtil {

    public static ResponseEntity<String> htmlError(){
        String sb = "<html>" +
                "<head>" +
                "</head>" +
                "<body style=\"font-size: 90px;\">" +
                "<h1 align='center'>Smart Door</h1>" +
                "<div style='text-align:center;'>" +
                "<br /><br />" +
                "<b>" +
                "Access Denied!" +
                "</b>" +
                "</b></body>" +
                "</html>";
        return ResponseEntity.ok(sb);
    }

    public static ResponseEntity<String> htmlAccessGranted( final String name){
        String sb = "<html>" +
                "<head>" +
                "</head>" +
                "<body style=\"font-size: 90px;\">" +
                "<h1 align='center'>Smart Door</h1>" +
                "<div style='text-align:center;'>" +
                "<br />" +
                "<b>" +
                "Welcome " + name + "!" +
                "</b><br />" +
                "</b></body>" +
                "</html>";
        return ResponseEntity.ok(sb);
    }

}
