package com.invexdijin.mspaymentgateway.application.ports.in;

import com.invexdijin.mspaymentgateway.application.core.domain.*;

import java.security.NoSuchAlgorithmException;

public interface CreatePreferenceInputPort {
    String createPayment(PaymentReference paymentReference);
    PayRequest createPayuPayment(PaymentReference paymentReference) throws NoSuchAlgorithmException;

    ConsolidatedResponse validateSignature(PayResponse payResponse) throws NoSuchAlgorithmException;
}
