package com.example.crm.mappers;

import com.example.crm.entity.model.Items;
import com.example.crm.entity.model.orderitemmodel.OrderItem;
import com.example.crm.entity.model.ordermodel.Order;
import com.example.crm.entity.dtos.request.orderitem.OrderItemRequestDTO;
import com.example.crm.entity.dtos.response.orderitem.OrderItemResponseDTO; // ← fix this
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItem toEntity(Order order, Items item, Integer quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setItem(item);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(item.getItemPrice().doubleValue());
        return orderItem;
    }

    public OrderItemResponseDTO toResponse(OrderItem orderItem) {
        return OrderItemResponseDTO.builder()
                .id(orderItem.getId())
                .itemId(orderItem.getItem().getItemId())
                .itemName(orderItem.getItem().getItemName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .totalPrice(orderItem.getQuantity() * orderItem.getPrice())
                .build();
    }
}