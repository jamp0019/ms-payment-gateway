package com.invexdijin.mspaymentgateway.application.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayResponse {

    private Integer merchantId;
    @JsonProperty("merchant_name")
    private String merchantName;
    @JsonProperty("merchant_address")
    private String merchantAddress;
    private String telephone;
    @JsonProperty("merchant_url")
    private String merchantUrl;
    private String transactionState;
    private String lapTransactionState;
    private String message;
    private String referenceCode;
    @JsonProperty("reference_pol")
    private String referencePol;
    private String transactionId;
    private String description;
    private String cus;
    private String orderLanguage;
    private String extra1;
    private String extra2;
    private String extra3;
    private String polTransactionState;
    private String signature;
    private String polResponseCode;
    private String lapResponseCode;
    private String risk;
    private String polPaymentMethod;
    private String lapPaymentMethod;
    private String polPaymentMethodType;
    private String lapPaymentMethodType;
    private String installmentsNumber;
    @JsonProperty("TX_VALUE")
    private String txValue;
    @JsonProperty("TX_TAX")
    private String txTax;
    private String currency;
    private String lng;
    private String pseCycle;
    private String buyerEmail;
    private String pseBank;
    private String pseReference1;
    private String pseReference2;
    private String pseReference3;
    private String authorizationCode;
    @JsonProperty("TX_ADMINISTRATIVE_FEE")
    private String txAdministrativeFee;
    @JsonProperty("TX_TAX_ADMINISTRATIVE_FEE")
    private String txTaxAdministrativeFee;
    @JsonProperty("TX_TAX_ADMINISTRATIVE_FEE_RETURN_BASE")
    private String txTaxAdministrativeFeeReturnBase;
    private Date processingDate;

}
