package com.invexdijin.mspaymentgateway.adapters.in.controller;

import com.invexdijin.mspaymentgateway.application.core.domain.*;
import com.invexdijin.mspaymentgateway.application.ports.in.CreatePreferenceInputPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/invexdijin")
@Slf4j
public class MercadoPagoController {

    @Autowired
    private CreatePreferenceInputPort createPreferenceInputPort;

    //@CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/create-payu-payment")
    ResponseEntity<?> createPayuMethodPayment(@Validated @RequestBody PaymentReference paymentReference) throws NoSuchAlgorithmException {
        PayRequest payRequest = createPreferenceInputPort.createPayuPayment(paymentReference);
        log.info("Pay request has been created!!!");
        return ResponseEntity.ok().body(payRequest);
    }

    //@CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/validate-signature")
    ResponseEntity<?> validatePayuSignature(@RequestBody PayResponse payResponse) throws NoSuchAlgorithmException {
        ConsolidatedResponse response = createPreferenceInputPort.validateSignature(payResponse);
        return ResponseEntity.ok().body(response);
    }

    //@CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/create-client")
    ResponseEntity<?> createPayment(@Valid @RequestBody PaymentReference paymentReference) {
        String response = createPreferenceInputPort.createPayment(paymentReference);
        log.info(paymentReference.getPaymentName()+" "+" save successfully");
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/notification-pay")
    ResponseEntity<?> notification(@RequestHeader MultiValueMap<String, String> headers,
                                   @RequestParam("response_code_pol") int responseCodePol,
                                   @RequestParam("phone") String phone,
                                   @RequestParam("additional_value") double additionalValue,
                                   @RequestParam("test") int test,
                                   @RequestParam("transaction_date") String transactionDate,
                                   @RequestParam("cc_number") String ccNumber,
                                   @RequestParam("cc_holder") String ccHolder,
                                   @RequestParam("error_code_bank") String errorCodeBank,
                                   @RequestParam("billing_country") String billingCountry,
                                   @RequestParam("bank_referenced_name") String bankReferencedName,
                                   @RequestParam("description") String description,
                                   @RequestParam("administrative_fee_tax") double administrativeFeeTax,
                                   @RequestParam("value") double value,
                                   @RequestParam("administrative_fee") double administrativeFee,
                                   @RequestParam("payment_method_type") int paymentMethodType,
                                   @RequestParam("office_phone") String officePhone,
                                   @RequestParam("email_buyer") String emailBuyer,
                                   @RequestParam("response_message_pol") String responseMessagePol,
                                   @RequestParam("error_message_bank") String errorMessageBank,
                                   @RequestParam("shipping_city") String shippingCity,
                                   @RequestParam("transaction_id") String transactionId,
                                   @RequestParam("sign") String sign,
                                   @RequestParam("tax") double tax,
                                   @RequestParam("payment_method") int paymentMethod,
                                   @RequestParam("billing_address") String billingAddress,
                                   @RequestParam("payment_method_name") String paymentMethodName,
                                   @RequestParam("pse_bank") String pseBank,
                                   @RequestParam("state_pol") int statePol,
                                   @RequestParam("date") String date,
                                   @RequestParam("nickname_buyer") String nicknameBuyer,
                                   @RequestParam("reference_pol") String referencePol,
                                   @RequestParam("currency") String currency,
                                   @RequestParam("risk") double risk,
                                   @RequestParam("shipping_address") String shippingAddress,
                                   @RequestParam("bank_id") int bankId,
                                   @RequestParam("payment_request_state") String paymentRequestState,
                                   @RequestParam("customer_number") String customerNumber,
                                   @RequestParam("administrative_fee_base") double administrativeFeeBase,
                                   @RequestParam("attempts") int attempts,
                                   @RequestParam("merchant_id") int merchantId,
                                   @RequestParam("exchange_rate") double exchangeRate,
                                   @RequestParam("shipping_country") String shippingCountry,
                                   @RequestParam("installments_number") int installmentsNumber,
                                   @RequestParam("franchise") String franchise,
                                   @RequestParam("payment_method_id") int paymentMethodId,
                                   @RequestParam("extra1") String extra1,
                                   @RequestParam("extra2") String extra2,
                                   @RequestParam("antifraudMerchantId") String antifraudMerchantId,
                                   @RequestParam("extra3") String extra3,
                                   @RequestParam("nickname_seller") String nicknameSeller,
                                   @RequestParam("ip") String ip,
                                   @RequestParam("airline_code") String airlineCode,
                                   @RequestParam("billing_city") String billingCity,
                                   @RequestParam("pse_reference1") String pseReference1,
                                   @RequestParam("reference_sale") String referenceSale,
                                   @RequestParam("pse_reference3") String pseReference3,
                                   @RequestParam("pse_reference2") String pseReference2
    ,HttpServletRequest request) throws NoSuchAlgorithmException {

        //ConsolidatedResponse response = createPreferenceInputPort.validateSignature(payResponse);
        return ResponseEntity.ok().body("response");
    }

}
