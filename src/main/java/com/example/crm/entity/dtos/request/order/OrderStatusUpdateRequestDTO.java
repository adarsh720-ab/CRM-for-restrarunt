package com.example.crm.entity.dtos.request.order;

import com.example.crm.util.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateRequestDTO {

    @NotNull(message = "Status is required")
    private OrderStatus status;
}