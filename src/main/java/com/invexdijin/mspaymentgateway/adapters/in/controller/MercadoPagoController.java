package com.invexdijin.mspaymentgateway.adapters.in.controller;

import com.invexdijin.mspaymentgateway.application.core.domain.Response;
import com.invexdijin.mspaymentgateway.application.ports.in.CreatePreferenceInputPort;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payment")
@Slf4j
public class MercadoPagoController {

    @Autowired
    private CreatePreferenceInputPort createPreferenceInputPort;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/create-preference")
    ResponseEntity<?> createPreference() throws MPException, MPApiException {
        Response response = createPreferenceInputPort.createPreference();
        return ResponseEntity.ok().body(response);
    }

}
