package com.example.crm.entity.dtos.request.payment;

import com.example.crm.util.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusUpdateRequestDTO {

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;

}
