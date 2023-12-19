package com.invexdijin.mspaymentgateway.application.core.domain;

import lombok.Data;

import java.util.Date;

@Data
public class InitSearch {

    private String id;

    private String documentType;

    private String documentNumber;

    private String firstName;

    private String lastName;

    private String fullName;

    private String cellphone;

    private String searchType;

    private Date searchDate;

}
