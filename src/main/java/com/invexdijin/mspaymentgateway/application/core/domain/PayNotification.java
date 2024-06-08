package com.invexdijin.mspaymentgateway.application.core.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayNotification {

    private Double value;
    private Integer merchantId;
    private String referenceSale;
    private String currency;
    private Integer statePol;
    private String sign;
    private String responseMessagePol;


}
