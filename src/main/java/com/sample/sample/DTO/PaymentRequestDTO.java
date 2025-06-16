package com.sample.sample.DTO;



import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String orderId;
    private String paymentId;
    private String signature;
    private Integer amount;
    private String currency;
    private String receipt;
}

