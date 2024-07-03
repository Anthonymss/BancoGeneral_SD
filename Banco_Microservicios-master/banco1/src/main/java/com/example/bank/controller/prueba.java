package com.example.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.discovery.DiscoveryClient;


import java.util.List;

@CrossOrigin("*")
@RestController
public class prueba {
    @GetMapping("/hello")
    public String prueba(){
        return "hola mundo,\nby Anthony_mss";
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/microservicios")
    public List<String> getServices() {
        return discoveryClient.getServices();
    }

}
