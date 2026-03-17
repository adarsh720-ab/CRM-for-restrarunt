package com.example.crm.entity.dtos.response.payment;

import com.example.crm.util.enums.PaymentMethod;
import com.example.crm.util.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {

    private UUID id;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentTime;

    // Order summary
    private UUID orderId;
    private String customerName;
    private Double totalAmount;
}