package com.invexdijin.mspaymentgateway.application.core.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaymentReference {

    @NotNull(message = "contact-name-error-message")
    @NotEmpty(message = "contact-name-error-message")
    @NotBlank(message = "contact-name-error-message")
    private String paymentName;

    @NotNull(message = "contact-lastname-error-message")
    @NotEmpty(message = "contact-lastname-error-message")
    @NotBlank(message = "contact-lastname-error-message")
    private String paymentLastName;

    @Email(message = "contact-mail-error-message-valid")
    @NotNull(message = "contact-mail-error-message")
    @NotEmpty(message = "contact-mail-error-message")
    @NotBlank(message = "contact-mail-error-message")
    @Size(min = 4, message = "contact-mail-error-message-characters")
    private String paymentEmail;

    @NotNull(message = "payment-document-type-error-message")
    @NotEmpty(message = "payment-document-type-error-message")
    @NotBlank(message = "payment-document-type-error-message")
    @Size(min = 2, message = "payment-document-type-error-message-characters")
    @Size(max = 2, message = "payment-document-type-error-message-characters-max")
    private String paymentDocumentType;

    @NotNull(message = "payment-document-number-error-message")
    @NotEmpty(message = "payment-document-number-error-message")
    @NotBlank(message = "payment-document-number-error-message")
    @Size(min = 7, message = "payment-document-number-error-message-characters")
    @Size(max = 10, message = "payment-document-number-error-message-characters-max")
    private String paymentDocumentNumber;

    @NotNull(message = "contact-number-message")
    @NotEmpty(message = "contact-number-message")
    @NotBlank(message = "contact-number-message")
    @Pattern(regexp = "\\d+",message = "contact-number-message-number")
    private String paymentContact;

    private String paymentSignature;

    private String paymentStatus;

    private InitSearch initSearch;

}
