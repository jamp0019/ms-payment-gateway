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

    @Value("${merchant.id}")
    private Integer merchantId;

    @Value("${account.id}")
    private Integer accountId;

    @Value("${description}")
    private String description;

    @Value("${amount}")
    private Long amount;

    @Value("${tax}")
    private Long tax;

    @Value("${tax.return.base}")
    private Long taxReturnBase;

    @Value("${currency}")
    private String currency;

    @Value("${test.property}")
    private Integer testProperty;

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
    public PayRequest createPayuPayment(PaymentReference paymentReference) {
        PayRequest payRequest = new PayRequest();
        try{
            payRequest.setMerchantId(merchantId);
            payRequest.setAccountId(accountId);
            payRequest.setDescription(description);
            payRequest.setAmount(amount);
            payRequest.setTax(tax);
            payRequest.setTaxReturnBase(taxReturnBase);
            payRequest.setCurrency(currency);
            payRequest.setTest(testProperty);
            payRequest.setBuyerEmail(paymentReference.getPaymentEmail());
            payRequest.setResponseUrl(responseUrl+paymentReference.getInitSearch().getId());
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
        log.info("Se valida signature");
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
                PaymentReference paymentReference = bdTransactionClient.updatePayment(payResponse.getReferenceCode(), "APPROVED");
                RequestSearch requestSearch = new RequestSearch();
                requestSearch.setPaymentName(paymentReference.getPaymentName());
                requestSearch.setPaymentEmail(paymentReference.getPaymentEmail());
                requestSearch.setSearchFullName(paymentReference.getInitSearch().getFullName());
                requestSearch.setSearchName(paymentReference.getInitSearch().getFirstName());
                requestSearch.setSearchLastName(paymentReference.getInitSearch().getLastName());
                requestSearch.setDocumentType(paymentReference.getInitSearch().getDocumentType());
                requestSearch.setDocumentNumber(paymentReference.getInitSearch().getDocumentNumber());
                //Disparar micro de busqueda(buscapersonas/ antecedentes)
                consolidatedResponse = utilOutPort.consumeSearchMethod(paymentReference.getInitSearch().getSearchType(),requestSearch);
                consolidatedResponse.setTransStatus(payResponse.getLapTransactionState());

            }
            else{
                log.info("DECLINED");
                //Haga actualizacion en la bd cuando el estado de la transacción es declinada
                bdTransactionClient.updatePayment(signatureResponse, "DECLINED");
            }
        } catch (Exception ex){
            log.error("Failed update with database or connection report services");
            throw new InternalServerError(ex.getMessage());
        }
        return consolidatedResponse;
    }


}
