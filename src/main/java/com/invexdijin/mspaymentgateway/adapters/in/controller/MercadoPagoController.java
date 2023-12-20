package com.invexdijin.mspaymentgateway.adapters.in.controller;

import com.invexdijin.mspaymentgateway.application.core.domain.*;
import com.invexdijin.mspaymentgateway.application.ports.in.CreatePreferenceInputPort;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/invexdijin")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Slf4j
public class MercadoPagoController {

    @Autowired
    private CreatePreferenceInputPort createPreferenceInputPort;

    @RequestMapping(method = RequestMethod.POST, path = "/create-payu-payment")
    ResponseEntity<?> createPayuMethodPayment(@Validated @RequestBody PaymentReference paymentReference) throws NoSuchAlgorithmException {
        PayRequest payRequest = createPreferenceInputPort.createPayuPayment(paymentReference);
        log.info("Pay request has been created!!!");
        return ResponseEntity.ok().body(payRequest);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/validate-signature")
    ResponseEntity<?> validatePayuSignature(@RequestBody PayResponse payResponse) throws NoSuchAlgorithmException {
        ConsolidatedResponse response = createPreferenceInputPort.validateSignature(payResponse);
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/create-client")
    ResponseEntity<?> createPayment(@Valid @RequestBody PaymentReference paymentReference) {
        String response = createPreferenceInputPort.createPayment(paymentReference);
        log.info(paymentReference.getPaymentName()+" "+" save successfully");
        return ResponseEntity.ok().body(response);
    }

}
