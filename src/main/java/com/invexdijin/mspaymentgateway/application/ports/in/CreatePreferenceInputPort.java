package com.invexdijin.mspaymentgateway.application.ports.in;

import com.invexdijin.mspaymentgateway.application.core.domain.Client;
import com.invexdijin.mspaymentgateway.application.core.domain.PayRequest;
import com.invexdijin.mspaymentgateway.application.core.domain.PayResponse;
import com.invexdijin.mspaymentgateway.application.core.domain.ValidateSignatureResponse;

import java.security.NoSuchAlgorithmException;

public interface CreatePreferenceInputPort {
    Client createClient(Client client);
    PayRequest createPayuPayment(String email) throws NoSuchAlgorithmException;

    ValidateSignatureResponse validateSignature(PayResponse payResponse) throws NoSuchAlgorithmException;
}
