package com.kam.springboot1.com.kam.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FirstController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/firstController")
    public ResponseEntity<String> getFirstController()
    {
        ResponseEntity<String> secondServiceResult = restTemplate.getForEntity("http://localhost:8081/secondProject/secondController", String.class);
        String secondServiceData = "Status Code - " + secondServiceResult.getStatusCode() + "   Body : " + secondServiceResult.getBody();

        ResponseEntity<String> thirdServiceResult = restTemplate.getForEntity("http://localhost:8082/thirdProject/thirdController", String.class);
        String thirdServiceData = "Status Code - " + thirdServiceResult.getStatusCode() + "   Body : " + thirdServiceResult.getBody();

        return ResponseEntity.of(Optional.of("Hello First Service - first controller" + "\n" + secondServiceData + "\n" + thirdServiceData));
    }
}
