package com.invexdijin.mspaymentgateway.adapters.in.controller;

import com.invexdijin.mspaymentgateway.application.core.domain.PayResponse;
import com.invexdijin.mspaymentgateway.application.ports.in.CreatePreferenceInputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/v1/payment")
@Slf4j
public class MercadoPagoController {

    @Autowired
    private CreatePreferenceInputPort createPreferenceInputPort;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/create-payu-payment")
    ResponseEntity<?> createPayuMethodPayment(@RequestParam String email) throws NoSuchAlgorithmException {
        //Preference preference = createPreferenceInputPort.createPreference();
        createPreferenceInputPort.createPayuPayment(email);
        return ResponseEntity.ok().body("preference");
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/validate-signature")
    ResponseEntity<?> validatePayuSignature(@RequestBody PayResponse payResponse) throws NoSuchAlgorithmException {
        //Preference preference = createPreferenceInputPort.createPreference();
        String result = createPreferenceInputPort.validateSignature(payResponse);
        return ResponseEntity.ok().body(result);
    }

}
