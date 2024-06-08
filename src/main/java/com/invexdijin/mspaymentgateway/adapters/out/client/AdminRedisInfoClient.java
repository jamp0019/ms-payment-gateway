package com.invexdijin.mspaymentgateway.adapters.out.client;

import com.invexdijin.mspaymentgateway.application.core.domain.ConsolidatedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "admin-redis",
        url = "${admin.redis}")
public interface AdminRedisInfoClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/invexdijin/payment-info/{signature}")
    ConsolidatedResponse getInfoIntoRedis(@PathVariable("signature") String signature);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/invexdijin/save-payment-info/{signature}")
    ConsolidatedResponse createInfoIntoRedis(@PathVariable("signature") String signature, @RequestBody ConsolidatedResponse consolidatedResponse);

}
