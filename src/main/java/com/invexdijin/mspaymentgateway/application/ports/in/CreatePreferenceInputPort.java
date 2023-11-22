package com.invexdijin.mspaymentgateway.application.ports.in;

import com.invexdijin.mspaymentgateway.application.core.domain.Response;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

public interface CreatePreferenceInputPort {
    Response createPreference() throws MPException, MPApiException;
}
