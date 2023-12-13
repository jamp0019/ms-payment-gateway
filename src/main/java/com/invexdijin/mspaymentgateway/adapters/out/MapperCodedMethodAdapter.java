package com.invexdijin.mspaymentgateway.adapters.out;

import com.invexdijin.mspaymentgateway.application.core.domain.PayResponse;
import com.invexdijin.mspaymentgateway.application.ports.out.MapperCodedMethodOutPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class MapperCodedMethodAdapter implements MapperCodedMethodOutPort {
    @Override
    public PayResponse mappingResponse(String preferenceId) {

        return null;
    }

    @Override
    public String mappingEncodedMethod(String input) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    @Override
    public String RoundHalfToEvent(String tx_value) {
        BigDecimal originalValue = new BigDecimal(tx_value);
        int scale = 1;
        RoundingMode roundingMode = RoundingMode.HALF_EVEN;
        return String.valueOf(originalValue.setScale(scale, roundingMode));
    }
}
