package com.example.crm.entity.dtos.response.order;

import com.example.crm.entity.dtos.response.orderitem.OrderItemResponseDTO;
import com.example.crm.util.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private UUID id;
    private LocalDateTime orderTime;
    private OrderStatus orderStatus;

    // Table summary
    private UUID tableId;
    private Integer tableNumber;

    // Customer summary
    private UUID customerId;
    private String customerName;
    private String customerContact;

    // Waiter summary
    private UUID waiterId;
    private String waiterName;
    private String waiterContact;

    // Order items
    private List<OrderItemResponseDTO> orderItems;

    // Total bill
    private Double totalAmount;
}