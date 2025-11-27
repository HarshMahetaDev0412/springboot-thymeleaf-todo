package com.app.todo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleClientIdLogger {

    public GoogleClientIdLogger(
            @Value("${spring.security.oauth2.client.registration.google.client-id}") String cidFromSpring,
            @Value("${spring.security.oauth2.client.registration.google.client-secret}") String csecFromSpring
            ) {

    	/*
        System.out.println(">>>> Spring resolved Google client-id = [" + cidFromSpring + "]");
        System.out.println(">>>> Length = " + cidFromSpring.length() + "]");

        System.out.println(">>>> Spring resolved Google client-secret = [" + csecFromSpring + "]");
        System.out.println(">>>> Length = " + csecFromSpring.length() + "]");
        */
    }
}
