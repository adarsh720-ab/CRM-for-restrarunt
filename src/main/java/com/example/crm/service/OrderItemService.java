package com.example.crm.service;


import com.example.crm.entity.dtos.request.orderitem.OrderItemRequestDTO;
import com.example.crm.entity.dtos.response.orderitem.OrderItemResponseDTO;

import java.util.List;
import java.util.UUID;

public interface OrderItemService {

    OrderItemResponseDTO addItemToOrder(UUID orderId, OrderItemRequestDTO request);

    List<OrderItemResponseDTO> getItemsByOrderId(UUID orderId);

    OrderItemResponseDTO getOrderItemById(UUID id);

    OrderItemResponseDTO updateOrderItem(UUID id, OrderItemRequestDTO request);

    void removeItemFromOrder(UUID id);
}