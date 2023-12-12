package com.invexdijin.mspaymentgateway.adapters.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class configuration {

    @Bean
    public HashMap<String, Object> useMap() {
        HashMap<String, Object> responseMap = new HashMap<>();
        return responseMap;
    }
}
