package com.invexdijin.mspaymentgateway.application.core.domain;

import lombok.Data;

@Data
public class PayRequest {

    private Integer merchantId;
    private Integer accountId;
    private String description;
    private String referenceCode;
    private Long amount;
    private Long tax;
    private Long taxReturnBase;
    private String currency;
    private String signature;
    private Integer test;
    private String buyerEmail;
    private String responseUrl;
    private String confirmationUrl;

}
