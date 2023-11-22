package com.invexdijin.mspaymentgateway.adapters.out;

import com.invexdijin.mspaymentgateway.application.core.domain.Response;
import com.invexdijin.mspaymentgateway.application.ports.out.MapperResponseOutPort;
import org.springframework.stereotype.Component;

@Component
public class MapperResponseAdapter implements MapperResponseOutPort {
    @Override
    public Response mappingResponse(String preferenceId) {

        return Response.builder()
                .preferenceId(preferenceId)
                .build();
    }
}
