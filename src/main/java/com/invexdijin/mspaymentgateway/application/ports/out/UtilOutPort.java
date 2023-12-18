package com.invexdijin.mspaymentgateway.application.ports.out;

import com.invexdijin.mspaymentgateway.application.core.domain.ConsolidatedResponse;
import com.invexdijin.mspaymentgateway.application.core.domain.RequestSearch;

import java.security.NoSuchAlgorithmException;
public interface UtilOutPort {
    ConsolidatedResponse consumeSearchMethod(String searchType, RequestSearch requestSearch);

    String mappingEncodedMethod(String input) throws NoSuchAlgorithmException;

    String RoundHalfToEvent(String tx_value);


}
