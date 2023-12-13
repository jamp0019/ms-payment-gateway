package com.invexdijin.mspaymentgateway.adapters.in.controller;

import com.invexdijin.mspaymentgateway.application.core.domain.PayRequest;
import com.invexdijin.mspaymentgateway.application.core.domain.PayResponse;
import com.invexdijin.mspaymentgateway.application.core.domain.ValidateSignatureResponse;
import com.invexdijin.mspaymentgateway.application.ports.in.CreatePreferenceInputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/v1/payment")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Slf4j
public class MercadoPagoController {

    @Autowired
    private CreatePreferenceInputPort createPreferenceInputPort;

    @RequestMapping(method = RequestMethod.POST, path = "/create-payu-payment")
    ResponseEntity<?> createPayuMethodPayment(@RequestParam String email) throws NoSuchAlgorithmException {
        //Preference preference = createPreferenceInputPort.createPreference();
        PayRequest payRequest = createPreferenceInputPort.createPayuPayment(email);
        log.info("Pay request has been created!!!");
        return ResponseEntity.ok().body(payRequest);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/validate-signature")
    ResponseEntity<?> validatePayuSignature(@RequestBody PayResponse payResponse) throws NoSuchAlgorithmException {
        //Preference preference = createPreferenceInputPort.createPreference();
        ValidateSignatureResponse response = createPreferenceInputPort.validateSignature(payResponse);
        return ResponseEntity.ok().body(response);
    }

}
