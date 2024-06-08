package com.invexdijin.mspaymentgateway.application.ports.in;

import com.invexdijin.mspaymentgateway.application.core.domain.*;

import java.security.NoSuchAlgorithmException;

public interface CreatePreferenceInputPort {
    String createPayment(PaymentReference paymentReference);
    PayRequest createPayuPayment(PaymentReference paymentReference) throws NoSuchAlgorithmException;

    ConsolidatedResponse responseValidateSignature(PayResponse payResponse) throws NoSuchAlgorithmException;

    void notificationValidateSignature(PayNotification payNotification) throws NoSuchAlgorithmException;
}

