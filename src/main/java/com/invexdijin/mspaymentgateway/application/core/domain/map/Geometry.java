package com.invexdijin.mspaymentgateway.application.core.domain.map;

import lombok.Data;

@Data
public class Geometry {

    private Viewport viewport;
    private Location location;
    private String locationType;

}
