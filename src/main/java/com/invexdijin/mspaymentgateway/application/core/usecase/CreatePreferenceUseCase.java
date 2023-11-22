package com.invexdijin.mspaymentgateway.application.core.usecase;

import com.invexdijin.mspaymentgateway.application.core.domain.Response;
import com.invexdijin.mspaymentgateway.application.core.exception.InternalServerException;
import com.invexdijin.mspaymentgateway.application.ports.in.CreatePreferenceInputPort;
import com.invexdijin.mspaymentgateway.application.ports.out.MapperResponseOutPort;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CreatePreferenceUseCase implements CreatePreferenceInputPort {

    @Autowired
    private MapperResponseOutPort mapperResponseOutPort;
    @Value("${access.token}")
    private String accessToken;

    @Value("${item.price}")
    private String itemPrice;

    private static final String successEndpoint="http://localhost:4200/success";
    private static final String pendingEndpoint="http://localhost:4200/pending";
    private static final String failureEndpoint="http://localhost:4200/failure";
    private static final String notifyEndpoint="https://localhost:4200/notify";
    @Override
    public Response createPreference() throws MPException, MPApiException {

        Response response = null;
        try{
            MercadoPagoConfig.setAccessToken(accessToken);
            PreferenceItemRequest itemRequest =
                    PreferenceItemRequest.builder()
                            .quantity(1)
                            .currencyId("COL")
                            .unitPrice(new BigDecimal(itemPrice))
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
            Preference preference = client.create(preferenceRequest);
            log.info("SandboxInitPoint--> "+preference.getSandboxInitPoint()+" or "+preference.getInitPoint());
            response = mapperResponseOutPort.mappingResponse(preference.getId());
        }catch(Exception ex){
            log.error("Preference can't be processed");
            throw new InternalServerException("Preference can't be processed. additional errors: "+ex.getMessage());
        }

        return response;
    }
}
