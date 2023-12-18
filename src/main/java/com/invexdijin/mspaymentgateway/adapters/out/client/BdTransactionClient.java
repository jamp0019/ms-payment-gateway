package com.invexdijin.mspaymentgateway.adapters.out.client;

import com.invexdijin.mspaymentgateway.application.core.domain.PaymentReference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bd-transaction",
        url = "http://localhost:8082")
public interface BdTransactionClient {
    @RequestMapping(method = RequestMethod.PUT, value = "/api/v1/invexdijin/update-payment")
    Object updateTransaction(@RequestParam("signature") String signature,
                             @RequestParam("transactionState") String transactionState);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/invexdijin/create-payment")
    String createTransaction(@RequestBody PaymentReference paymentReference);
}
