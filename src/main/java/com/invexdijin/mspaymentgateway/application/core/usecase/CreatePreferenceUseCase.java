package com.invexdijin.mspaymentgateway.application.core.usecase;

import com.invexdijin.mspaymentgateway.adapters.out.client.BdTransactionClient;
import com.invexdijin.mspaymentgateway.adapters.out.client.MsAntecedentReportClient;
import com.invexdijin.mspaymentgateway.adapters.out.repository.ClientRepository;
import com.invexdijin.mspaymentgateway.application.core.domain.Client;
import com.invexdijin.mspaymentgateway.application.core.domain.PayRequest;
import com.invexdijin.mspaymentgateway.application.core.domain.PayResponse;
import com.invexdijin.mspaymentgateway.application.core.domain.ValidateSignatureResponse;
import com.invexdijin.mspaymentgateway.application.core.exception.InternalServerException;
import com.invexdijin.mspaymentgateway.application.ports.in.CreatePreferenceInputPort;
import com.invexdijin.mspaymentgateway.application.ports.out.MapperCodedMethodOutPort;
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

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BdTransactionClient bdTransactionClient;

    @Autowired
    private MsAntecedentReportClient msAntecedentReportClient;

    @Value("${access.token}")
    private String accessToken;

    @Value("${item.price}")
    private String itemPrice;

    @Override
    public Client createClient(Client client) {
        client = clientRepository.save(client);
        return client;
    }

    @Override
    public PayRequest createPayuPayment(String email) throws NoSuchAlgorithmException {
        PayRequest payRequest = new PayRequest();
        payRequest.setMerchantId(508029);
        payRequest.setAccountId(512321);
        payRequest.setDescription("Test PAYU");
        payRequest.setAmount(20000L);
        payRequest.setTax(3193L);
        payRequest.setTaxReturnBase(16806L);
        payRequest.setCurrency("COP");
        payRequest.setTest(1);
        payRequest.setBuyerEmail("test@test.com");
        payRequest.setResponseUrl("http://localhost:4200/after-payment");
        payRequest.setConfirmationUrl("http://www.test.com/confirmation");
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
    public ValidateSignatureResponse validateSignature(PayResponse payResponse) throws NoSuchAlgorithmException {


        ValidateSignatureResponse validateSignatureResponse = new ValidateSignatureResponse();
        String result="";
        String rounding_tx_value = mapperCodedMethodOutPort.RoundHalfToEvent(payResponse.getTxValue());
        String input = "4Vj8eK4rloUd272L48hsrarnUA"+"~"+
                payResponse.getMerchantId()+"~"+
                payResponse.getReferenceCode()+"~"+
                rounding_tx_value +"~"+
                payResponse.getCurrency() +"~"+
                payResponse.getTransactionState();
        String signatureResponse = mapperCodedMethodOutPort.mappingEncodedMethod(input);
        if(signatureResponse.equals(payResponse.getSignature()) || payResponse.getLapTransactionState().equals("APPROVED")){
            result="APPROVED";
            //Haga actualizacion en la bd
            Object genericObject = bdTransactionClient.updateTransaction(signatureResponse);
            //Disparar micro de busqueda(buscapersonas/ antecedentes)
            if(genericObject.equals("search-person")){
                msAntecedentReportClient.requestSearchPerson(null);
            }else{
                msAntecedentReportClient.requestAntecedentReport(null);
            }
        }
        else{
            result="DECLINED";
        }
        validateSignatureResponse.setResponse(result);
        return validateSignatureResponse;
    }


}
