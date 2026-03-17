package com.example.crm.entity.dtos.response.orderitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDTO {

    private UUID id;
    private UUID itemId;
    private String itemName;
    private Integer quantity;
    private Double price;
    private Double totalPrice; // quantity * price
}