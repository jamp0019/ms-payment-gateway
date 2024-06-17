package com.invexdijin.mspaymentgateway.application.core.usecase;

import com.invexdijin.mspaymentgateway.adapters.out.client.AdminRedisInfoClient;
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
    private AdminRedisInfoClient adminRedisInfoClient;

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

    @Value("${amount.search.person}")
    private Long amountSearchPerson;

    @Value("${tax.search.person}")
    private Long taxSearchPerson;

    @Value("${tax.return.base.search.person}")
    private Long taxReturnBaseSearchPerson;

    @Value("${amount.antecedent.report}")
    private Long amountAntecedentReport;

    @Value("${tax.antecedent.report}")
    private Long taxAntecedentReport;

    @Value("${tax.return.base.antecedent.report}")
    private Long taxReturnBaseAntecedentReport;
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
        log.info(paymentReference.getInitSearch().toString());
        PayRequest payRequest = new PayRequest();
        try{
            payRequest.setMerchantId(merchantId);
            payRequest.setAccountId(accountId);
            payRequest.setDescription(description);
            if(paymentReference.getInitSearch().getSearchType().equals("people")){
                payRequest.setAmount(amountSearchPerson);
                payRequest.setTax(taxSearchPerson);
                payRequest.setTaxReturnBase(taxReturnBaseSearchPerson);
            } else {
                payRequest.setAmount(amountAntecedentReport);
                payRequest.setTax(taxAntecedentReport);
                payRequest.setTaxReturnBase(taxReturnBaseAntecedentReport);
            }
            payRequest.setCurrency(currency);
            payRequest.setTest(testProperty);
            payRequest.setBuyerEmail(paymentReference.getPaymentEmail());
            payRequest.setResponseUrl(responseUrl+paymentReference.getInitSearch().getId());
            payRequest.setConfirmationUrl(confirmationUrl);
            payRequest.setReferenceCode(paymentReference.getInitSearch().getId());
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
    public ConsolidatedResponse responseValidateSignature(PayResponse payResponse) throws NoSuchAlgorithmException {
        log.info("Se valida signature");
        ConsolidatedResponse consolidatedResponse=null;
        try{
            String rounding_tx_value = utilOutPort.RoundHalfToEvent(payResponse.getTxValue());
            String input = apiKey+"~"+
                    payResponse.getMerchantId()+"~"+
                    payResponse.getReferenceCode()+"~"+
                    rounding_tx_value +"~"+
                    payResponse.getCurrency() +"~"+
                    payResponse.getTransactionState();
            String signatureResponse = utilOutPort.mappingEncodedMethod(input);
            //
            if(signatureResponse.equals(payResponse.getSignature()) || payResponse.getLapTransactionState().equals("APPROVED")){
                log.info("APPROVED");
                bdTransactionClient.updatePayment(payResponse.getReferenceSale(), "APPROVED");
                consolidatedResponse = adminRedisInfoClient.getInfoIntoRedis(payResponse.getSignature());
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

    @Override
    public void notificationValidateSignature(PayNotification payNotification) throws NoSuchAlgorithmException {
        log.info("Se valida signature");
        ConsolidatedResponse consolidatedResponse=null;
        try{
            String rounding_tx_value = utilOutPort.RoundHalfToEvent(String.valueOf(payNotification.getValue()));
            String input = apiKey+"~"+
                    payNotification.getMerchantId()+"~"+
                    payNotification.getReferenceSale()+"~"+
                    rounding_tx_value +"~"+
                    payNotification.getCurrency() +"~"+
                    payNotification.getStatePol();
            String signatureResponse = utilOutPort.mappingEncodedMethod(input);
            //
            if(signatureResponse.equals(payNotification.getSign()) || payNotification.getResponseMessagePol().equals("APPROVED")){
                log.info("APPROVED");
                //Haga actualizacion en la bd cuando el estado de la transacción es aprobada
                PaymentReference paymentReference = bdTransactionClient.updatePayment(payNotification.getReferenceSale(), "APPROVED-SE");
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
                consolidatedResponse.setTransStatus(payNotification.getResponseMessagePol());
                adminRedisInfoClient.createInfoIntoRedis(payNotification.getSign(), consolidatedResponse);
                log.info("ConsolidatedResponse Object has been saved");
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
    }


}
