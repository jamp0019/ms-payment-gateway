package com.invexdijin.mspaymentgateway.application.core.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PayResponse {

    private Integer merchantId;
    private String referenceCode;
    private String TX_VALUE;
    private String currency;
    private String transactionState;
    private String signature;
    private String reference_pol;
    private String cus;
    private String description;
    private String pseBank;

}
