package com.sample.sample.Model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String paymentId;
    private String signature;

    private Integer amount;
    private String currency;
    private String receipt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // SUCCESS or FAILED
}

