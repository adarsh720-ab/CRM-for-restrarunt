package com.example.crm.service.impl;

import com.example.crm.entity.dtos.request.orderitem.OrderItemRequestDTO;
import com.example.crm.entity.dtos.response.orderitem.OrderItemResponseDTO;
import com.example.crm.entity.model.Items;
import com.example.crm.entity.model.orderitemmodel.OrderItem;
import com.example.crm.entity.model.ordermodel.Order;
import com.example.crm.mappers.OrderItemMapper;
import com.example.crm.repository.ItemRepository;
import com.example.crm.repository.OrderItemRepository;
import com.example.crm.repository.OrderRepository;
import com.example.crm.service.OrderItemService;
import com.example.crm.util.enums.OrderStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemMapper orderItemMapper;

    // ─── Create ───────────────────────────────────────────────────

    @Override
    @Transactional
    public OrderItemResponseDTO addItemToOrder(UUID orderId, OrderItemRequestDTO request) {
        log.info("Adding item to orderId: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));

        if (order.getOrderStatus() == OrderStatus.COMPLETED
                || order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot add items to an order with status: " + order.getOrderStatus());
        }

        Items item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item not found with id: " + request.getItemId()));

        OrderItem orderItem = orderItemMapper.toEntity(order, item, request.getQuantity());
        OrderItem saved = orderItemRepository.save(orderItem);

        log.info("Item added successfully with orderItemId: {}", saved.getId());
        return orderItemMapper.toResponse(saved);
    }

    // ─── Read ─────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponseDTO> getItemsByOrderId(UUID orderId) {
        log.info("Fetching items for orderId: {}", orderId);
        return orderItemRepository.findByOrder_Id(orderId)
                .stream()
                .map(orderItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponseDTO getOrderItemById(UUID id) {
        log.info("Fetching order item with id: {}", id);
        return orderItemRepository.findById(id)
                .map(orderItemMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found with id: " + id));
    }

    // ─── Update ───────────────────────────────────────────────────

    @Override
    @Transactional
    public OrderItemResponseDTO updateOrderItem(UUID id, OrderItemRequestDTO request) {
        log.info("Updating order item with id: {}", id);

        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found with id: " + id));

        if (orderItem.getOrder().getOrderStatus() == OrderStatus.COMPLETED
                || orderItem.getOrder().getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot update items of an order with status: "
                            + orderItem.getOrder().getOrderStatus());
        }

        Items item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item not found with id: " + request.getItemId()));

        orderItem.setItem(item);
        orderItem.setQuantity(request.getQuantity());
        orderItem.setPrice(item.getItemPrice().doubleValue());

        OrderItem updated = orderItemRepository.save(orderItem);
        log.info("Order item updated successfully with id: {}", updated.getId());
        return orderItemMapper.toResponse(updated);
    }

    // ─── Delete ───────────────────────────────────────────────────

    @Override
    @Transactional
    public void removeItemFromOrder(UUID id) {
        log.info("Removing order item with id: {}", id);

        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found with id: " + id));

        if (orderItem.getOrder().getOrderStatus() == OrderStatus.COMPLETED
                || orderItem.getOrder().getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot remove items from an order with status: "
                            + orderItem.getOrder().getOrderStatus());
        }

        orderItemRepository.delete(orderItem);
        log.info("Order item removed successfully with id: {}", id);
    }
}