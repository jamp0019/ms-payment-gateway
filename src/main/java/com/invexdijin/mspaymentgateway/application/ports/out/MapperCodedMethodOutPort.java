package com.invexdijin.mspaymentgateway.application.ports.out;

import com.invexdijin.mspaymentgateway.application.core.domain.PayResponse;
import java.security.NoSuchAlgorithmException;
public interface MapperCodedMethodOutPort {
    PayResponse mappingResponse(String preferenceId);

    String mappingEncodedMethod(String input) throws NoSuchAlgorithmException;

    String RoundHalfToEvent(String tx_value);
}
