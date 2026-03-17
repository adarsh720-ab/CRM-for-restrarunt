package com.example.crm.mappers;

import com.example.crm.entity.AppUser;
import com.example.crm.entity.Table;
import com.example.crm.entity.dtos.response.order.OrderResponseDTO;
import com.example.crm.entity.dtos.response.orderitem.OrderItemResponseDTO;
import com.example.crm.entity.model.Items;
import com.example.crm.entity.model.ordermodel.Order;
import com.example.crm.entity.model.orderitemmodel.OrderItem;
import com.example.crm.util.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    /**
     * Maps Order entity to OrderResponseDTO.
     * Flattens table, customer, waiter and orderItems into the response.
     */
    public OrderResponseDTO toResponse(Order order) {
        List<OrderItemResponseDTO> itemResponses = order.getOrderItems()
                .stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());

        Double totalAmount = itemResponses.stream()
                .mapToDouble(OrderItemResponseDTO::getTotalPrice)
                .sum();

        return OrderResponseDTO.builder()
                .id(order.getId())
                .orderTime(order.getOrderTime())
                .orderStatus(order.getOrderStatus())
                .tableId(order.getTable().getTableId())
                .tableNumber(order.getTable().getTableNumber())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getName())
                .customerContact(order.getCustomer().getContact())
                .waiterId(order.getWaiter().getId())
                .waiterName(order.getWaiter().getName())
                .waiterContact(order.getWaiter().getContact())
                .orderItems(itemResponses)
                .totalAmount(totalAmount)
                .build();
    }

    /**
     * Maps OrderItem entity to OrderItemResponseDTO.
     */
    public OrderItemResponseDTO toItemResponse(OrderItem orderItem) {
        return OrderItemResponseDTO.builder()
                .id(orderItem.getId())
                .itemId(orderItem.getItem().getItemId())
                .itemName(orderItem.getItem().getItemName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .totalPrice(orderItem.getQuantity() * orderItem.getPrice())
                .build();
    }

    /**
     * Creates a new Order entity.
     * Table, customer and waiter must be fetched in the service before calling this.
     */
    public Order toEntity(Table table, AppUser customer, AppUser waiter) {
        Order order = new Order();
        order.setOrderTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.CREATED);
        order.setTable(table);
        order.setCustomer(customer);
        order.setWaiter(waiter);
        return order;
    }

    /**
     * Creates an OrderItem entity for the given order and menu item.
     */
    public OrderItem toItemEntity(Order order, Items item, Integer quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setItem(item);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(item.getItemPrice().doubleValue());
        return orderItem;
    }
}