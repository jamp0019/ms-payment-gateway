package com.invexdijin.mspaymentgateway.application.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Client {

    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
}
