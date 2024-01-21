package com.invexdijin.mspaymentgateway.adapters.out.client;

import com.invexdijin.mspaymentgateway.application.core.domain.ConsolidatedResponse;
import com.invexdijin.mspaymentgateway.application.core.domain.RequestSearch;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*@FeignClient(value = "ms-antecedent-report",
        url = "http://ms-antecedent-report:80")*/
@FeignClient(value = "ms-antecedent-report",
        url = "http://localhost:8080")
public interface MsAntecedentReportClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/invexdijin/search-person-report")
    ConsolidatedResponse requestSearchPerson(@RequestBody RequestSearch requestSearch);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/invexdijin/antecedent-report")
    ConsolidatedResponse requestAntecedentReport(@RequestBody RequestSearch requestSearch);
}
