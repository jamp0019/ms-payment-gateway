package com.invexdijin.mspaymentgateway.application.core.domain;

import lombok.Data;

@Data
public class RequestSearch {

    private String name;
    private String email;
    private String documentType;
    private String documentNumber;

}
