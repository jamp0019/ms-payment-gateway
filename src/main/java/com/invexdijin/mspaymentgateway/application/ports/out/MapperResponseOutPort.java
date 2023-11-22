package com.invexdijin.mspaymentgateway.application.ports.out;

import com.invexdijin.mspaymentgateway.application.core.domain.Response;

public interface MapperResponseOutPort {
    Response mappingResponse(String preferenceId);
}
