package com.invexdijin.mspaymentgateway.application.core.usecase;

import com.invexdijin.mspaymentgateway.adapters.out.client.BdTransactionClient;
import com.invexdijin.mspaymentgateway.adapters.out.client.MsAntecedentReportClient;
import com.invexdijin.mspaymentgateway.application.core.domain.*;
import com.invexdijin.mspaymentgateway.application.core.exception.InternalServerError;
import com.invexdijin.mspaymentgateway.application.ports.in.CreatePreferenceInputPort;
import com.invexdijin.mspaymentgateway.application.ports.out.UtilOutPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class CreatePreferenceUseCase implements CreatePreferenceInputPort {

    @Autowired
    private UtilOutPort utilOutPort;

    @Autowired
    private BdTransactionClient bdTransactionClient;

    @Autowired
    private MsAntecedentReportClient msAntecedentReportClient;

    @Value("${access.token}")
    private String accessToken;

    @Value("${item.price}")
    private String itemPrice;

    @Value("${api.key}")
    private String apiKey;

    @Value("${response.url}")
    private String responseUrl;

    @Value("${confirmation.url}")
    private String confirmationUrl;

    @Override
    public String createPayment(PaymentReference paymentReference) {
        try{
            String object = bdTransactionClient.createTransaction(paymentReference);
        } catch (Exception ex){
            log.error("Failed connection to bd transaction service");
            throw new InternalServerError(ex.getMessage());
        }
        return "OK";
    }

    @Override
    public PayRequest createPayuPayment(PaymentReference paymentReference) throws NoSuchAlgorithmException {
        PayRequest payRequest = new PayRequest();
        try{
            payRequest.setMerchantId(508029);
            payRequest.setAccountId(512321);
            payRequest.setDescription("Test PAYU");
            payRequest.setAmount(20000L);
            payRequest.setTax(3193L);
            payRequest.setTaxReturnBase(16806L);
            payRequest.setCurrency("COP");
            payRequest.setTest(1);
            payRequest.setBuyerEmail(paymentReference.getPaymentEmail());
            payRequest.setResponseUrl(responseUrl+paymentReference.getInitSearchId());
            payRequest.setConfirmationUrl(confirmationUrl);
            String input = apiKey+"~"+
                    payRequest.getMerchantId()+"~"+
                    payRequest.getReferenceCode()+"~"+
                    payRequest.getAmount() +"~"+
                    payRequest.getCurrency();
            String signature = utilOutPort.mappingEncodedMethod(input);
            payRequest.setSignature(signature);
        }catch (Exception ex){
            log.error("Failed mapping pay request");
            throw new InternalServerError(ex.getMessage());
        }
        return payRequest;
    }

    @Override
    public ConsolidatedResponse validateSignature(PayResponse payResponse) throws NoSuchAlgorithmException {

        ConsolidatedResponse consolidatedResponse=null;
        try{
            String rounding_tx_value = utilOutPort.RoundHalfToEvent(payResponse.getTxValue());
            String input = "4Vj8eK4rloUd272L48hsrarnUA"+"~"+
                    payResponse.getMerchantId()+"~"+
                    payResponse.getReferenceCode()+"~"+
                    rounding_tx_value +"~"+
                    payResponse.getCurrency() +"~"+
                    payResponse.getTransactionState();
            String signatureResponse = utilOutPort.mappingEncodedMethod(input);

            if(signatureResponse.equals(payResponse.getSignature()) || payResponse.getLapTransactionState().equals("APPROVED")){
                log.info("APPROVED");
                //Haga actualizacion en la bd cuando el estado de la transacción es aprobada
                Object genericObject = bdTransactionClient.updateTransaction(signatureResponse, "APPROVED");
                //Disparar micro de busqueda(buscapersonas/ antecedentes)
                consolidatedResponse = utilOutPort.consumeSearchMethod("",null);
            }
            else{
                log.info("DECLINED");
                //Haga actualizacion en la bd cuando el estado de la transacción es declinada
                bdTransactionClient.updateTransaction(signatureResponse, "DECLINED");
            }
        } catch (Exception ex){
            log.error("Failed update with database or connection report services");
            throw new InternalServerError(ex.getMessage());
        }
        return consolidatedResponse;
    }


}
