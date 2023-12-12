package com.invexdijin.mspaymentgateway.application.core.usecase;

import com.invexdijin.mspaymentgateway.application.core.domain.PayRequest;
import com.invexdijin.mspaymentgateway.application.core.domain.PayResponse;
import com.invexdijin.mspaymentgateway.application.core.exception.InternalServerException;
import com.invexdijin.mspaymentgateway.application.ports.in.CreatePreferenceInputPort;
import com.invexdijin.mspaymentgateway.application.ports.out.MapperCodedMethodOutPort;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CreatePreferenceUseCase implements CreatePreferenceInputPort {

    @Autowired
    private MapperCodedMethodOutPort mapperCodedMethodOutPort;
    @Value("${access.token}")
    private String accessToken;

    @Value("${item.price}")
    private String itemPrice;

    private static final String successEndpoint="http://localhost:4200/success";
    private static final String pendingEndpoint="http://localhost:4200/pending";
    private static final String failureEndpoint="http://localhost:4200/failure";
    private static final String notifyEndpoint="https://localhost:4200/notify";
    @Override
    public Preference createPreference() throws MPException, MPApiException {

        Preference preference = null;
        try{
            MercadoPagoConfig.setAccessToken(accessToken);
            PreferenceItemRequest itemRequest =
                    PreferenceItemRequest.builder()
                            .id("123")
                            .title("Test")
                            .description("Test")
                            .pictureUrl("www.picture.com")
                            .categoryId("1")
                            .quantity(1)
                            .currencyId("COL")
                            .unitPrice(new BigDecimal(2000))
                            .build();
            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);
            PreferenceBackUrlsRequest backUrlsRequest =
                    PreferenceBackUrlsRequest.builder()
                            .success(successEndpoint)
                            .pending(pendingEndpoint)
                            .failure(failureEndpoint)
                            .build();
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrlsRequest)
                    //.autoReturn("approved")
                    .notificationUrl(notifyEndpoint)
                    .build();
            PreferenceClient client = new PreferenceClient();
            preference = client.create(preferenceRequest);
            log.info("SandboxInitPoint--> "+preference.getSandboxInitPoint()+" or "+preference.getInitPoint());
            //response = mapperResponseOutPort.mappingResponse(preference.getId());
        }catch(Exception ex){
            log.error("Preference can't be processed");
            throw new InternalServerException("Preference can't be processed. additional errors: "+ex.getMessage());
        }

        return preference;
    }

    @Override
    public PayRequest createPayuPayment(String email) throws NoSuchAlgorithmException {
        PayRequest payRequest = new PayRequest();
        payRequest.setMerchantId(508029);
        payRequest.setAccountId(1009575);
        payRequest.setDescription("Test PAYU");
        payRequest.setReferenceCode("TestPayU");
        payRequest.setAmount(20000L);
        payRequest.setTax(3800L);
        payRequest.setTaxReturnBase(16200L);
        payRequest.setCurrency("COP");
        payRequest.setTest(1);
        payRequest.setBuyerEmail(email);
        payRequest.setResponseUrl("http://localhost:4200/success");
        payRequest.setConfirmationUrl("xxxxxx");
        String input = "4Vj8eK4rloUd272L48hsrarnUA"+"~"+
                payRequest.getMerchantId()+"~"+
                payRequest.getReferenceCode()+"~"+
                payRequest.getAmount() +"~"+
                payRequest.getCurrency();
        String signature = mapperCodedMethodOutPort.mappingEncodedMethod(input);
        payRequest.setSignature(signature);
        return payRequest;
    }

    @Override
    public String validateSignature(PayResponse payResponse) throws NoSuchAlgorithmException {
        String result="";
        String rounding_tx_value = mapperCodedMethodOutPort.RoundHalfToEvent(payResponse.getTX_VALUE());
        String input = "4Vj8eK4rloUd272L48hsrarnUA"+"~"+
                payResponse.getMerchantId()+"~"+
                payResponse.getReferenceCode()+"~"+
                rounding_tx_value +"~"+
                payResponse.getCurrency() +"~"+
                payResponse.getTransactionState();
        String signatureResponse = mapperCodedMethodOutPort.mappingEncodedMethod(input);
        if(signatureResponse.equals(payResponse.getSignature())){
            result="APPROVED";
        }
        else{
            result="DECLINED";
        }
        return result;
    }


}
