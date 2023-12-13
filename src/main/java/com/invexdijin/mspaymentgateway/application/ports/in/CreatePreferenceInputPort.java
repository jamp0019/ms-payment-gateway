package com.invexdijin.mspaymentgateway.application.ports.in;

import com.invexdijin.mspaymentgateway.application.core.domain.PayRequest;
import com.invexdijin.mspaymentgateway.application.core.domain.PayResponse;
import com.invexdijin.mspaymentgateway.application.core.domain.ValidateSignatureResponse;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import java.security.NoSuchAlgorithmException;

public interface CreatePreferenceInputPort {
    Preference createPreference() throws MPException, MPApiException;
    PayRequest createPayuPayment(String email) throws NoSuchAlgorithmException;

    ValidateSignatureResponse validateSignature(PayResponse payResponse) throws NoSuchAlgorithmException;
}
