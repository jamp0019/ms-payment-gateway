package com.invexdijin.mspaymentgateway.adapters.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bd-transaction",
        url = "http://localhost:8082")
public interface BdTransactionClient {
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/invexdijin/update-transaction")
    Object updateTransaction(@RequestParam("signature") String signature);
}
