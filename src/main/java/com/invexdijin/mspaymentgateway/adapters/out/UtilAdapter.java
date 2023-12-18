package com.invexdijin.mspaymentgateway.adapters.out;

import com.invexdijin.mspaymentgateway.adapters.out.client.MsAntecedentReportClient;
import com.invexdijin.mspaymentgateway.application.core.domain.ConsolidatedResponse;
import com.invexdijin.mspaymentgateway.application.core.domain.RequestSearch;
import com.invexdijin.mspaymentgateway.application.ports.out.UtilOutPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class UtilAdapter implements UtilOutPort {

    @Autowired
    private MsAntecedentReportClient msAntecedentReportClient;

    @Override
    public ConsolidatedResponse consumeSearchMethod(String searchType, RequestSearch requestSearch) {
        ConsolidatedResponse consolidatedResponse = null;
        switch(searchType) {
            case "0":
                consolidatedResponse = msAntecedentReportClient.requestSearchPerson(requestSearch);
                break;
            case "1":
                consolidatedResponse = msAntecedentReportClient.requestAntecedentReport(requestSearch);
                break;
            default:
                log.info("Request invalid");
        }
        return consolidatedResponse;
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
