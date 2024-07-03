package com.example.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@CrossOrigin("*")
@RestController
public class PruebasRemotasController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/bank-to-bank/{banco}")
    public String communicateWithBank2(@PathVariable String banco) {
        String serviceUrl = discoveryClient.getInstances(banco)
                .stream()
                .findFirst()
                .map(si -> si.getUri().toString())
                .orElseThrow(() -> new RuntimeException("Bank no está disponible"));

        String url = serviceUrl + "/banco/info";
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "Error al comunicarse con el servicio " + banco + ": " + e.getMessage();
        }
    }
}
