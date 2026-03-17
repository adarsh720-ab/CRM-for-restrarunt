package com.example.crm.service;

import com.example.crm.entity.dtos.request.order.OrderRequestDTO;
import com.example.crm.entity.dtos.response.order.OrderResponseDTO;
import com.example.crm.util.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO request);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO getOrderById(UUID id);

    List<OrderResponseDTO> getOrdersByStatus(OrderStatus status);

    List<OrderResponseDTO> getOrdersByCustomer(UUID customerId);

    List<OrderResponseDTO> getOrdersByWaiter(UUID waiterId);

    List<OrderResponseDTO> getOrdersByTable(UUID tableId);

    OrderResponseDTO updateOrderStatus(UUID id, OrderStatus status);

    void cancelOrder(UUID id);
}
