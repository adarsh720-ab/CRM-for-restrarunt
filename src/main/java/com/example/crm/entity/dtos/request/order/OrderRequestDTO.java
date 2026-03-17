package com.example.crm.entity.dtos.request.order;

import com.example.crm.entity.dtos.request.orderitem.OrderItemRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    @NotNull(message = "Table ID is required")
    private UUID tableId;

    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotNull(message = "Waiter ID is required")
    private UUID waiterId;

    @NotNull(message = "Order items are required")
    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemRequestDTO> orderItems;
}