package com.example.crm.service.impl;

import com.example.crm.entity.AppUser;
import com.example.crm.entity.Table;
import com.example.crm.entity.dtos.request.order.OrderRequestDTO;
import com.example.crm.entity.dtos.request.orderitem.OrderItemRequestDTO;
import com.example.crm.entity.dtos.response.order.OrderResponseDTO;
import com.example.crm.entity.model.Items;
import com.example.crm.entity.model.orderitemmodel.OrderItem;
import com.example.crm.entity.model.ordermodel.Order;
import com.example.crm.mappers.OrderItemMapper;
import com.example.crm.mappers.OrderMapper;
import com.example.crm.repository.AppUserRepository;
import com.example.crm.repository.ItemRepository;
import com.example.crm.repository.OrderItemRepository;
import com.example.crm.repository.OrderRepository;
import com.example.crm.repository.RestaurantTableRepository;
import com.example.crm.service.OrderService;
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
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AppUserRepository appUserRepository;
    private final RestaurantTableRepository tableRepository;
    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    // ─── Create ───────────────────────────────────────────────────

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        log.info("Creating order for customerId: {} on tableId: {}",
                request.getCustomerId(), request.getTableId());

        Table table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Table not found with id: " + request.getTableId()));

        AppUser customer = appUserRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with id: " + request.getCustomerId()));

        AppUser waiter = appUserRepository.findById(request.getWaiterId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Waiter not found with id: " + request.getWaiterId()));

        Order order = orderMapper.toEntity(table, customer, waiter);

        // Build order items from request and attach before saving so cascade persists them
        List<OrderItem> items = request.getOrderItems().stream()
                .map(itemReq -> mapToOrderItem(order, itemReq))
                .collect(Collectors.toList());

        order.setOrderItems(items);

        Order savedOrder = orderRepository.save(order);

        log.info("Order created successfully with id: {}", savedOrder.getId());
        return orderMapper.toResponse(savedOrder);
    }

    private OrderItem mapToOrderItem(Order order, OrderItemRequestDTO itemRequest) {
        Items item = itemRepository.findById(itemRequest.getItemId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item not found with id: " + itemRequest.getItemId()));
        return orderItemMapper.toEntity(order, item, itemRequest.getQuantity());
    }

    // ─── Read ─────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(UUID id) {
        log.info("Fetching order with id: {}", id);
        return orderRepository.findById(id)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        log.info("Fetching orders with status: {}", status);
        return orderRepository.findByOrderStatus(status)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByCustomer(UUID customerId) {
        log.info("Fetching orders for customerId: {}", customerId);
        return orderRepository.findByCustomer_Id(customerId)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByWaiter(UUID waiterId) {
        log.info("Fetching orders for waiterId: {}", waiterId);
        return orderRepository.findByWaiter_Id(waiterId)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByTable(UUID tableId) {
        log.info("Fetching orders for tableId: {}", tableId);
        return orderRepository.findByTableId(tableId)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ─── Update ───────────────────────────────────────────────────

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(UUID id, OrderStatus newStatus) {
        log.info("Updating status of order id: {} to {}", id, newStatus);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + id));

        validateStatusTransition(order.getOrderStatus(), newStatus);
        order.setOrderStatus(newStatus);

        Order updated = orderRepository.save(order);
        log.info("Order status updated to {} for id: {}", newStatus, id);
        return orderMapper.toResponse(updated);
    }

    // ─── Cancel ───────────────────────────────────────────────────

    @Override
    @Transactional
    public void cancelOrder(UUID id) {
        log.info("Cancelling order with id: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + id));

        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed order");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        log.info("Order cancelled successfully with id: {}", id);
    }

    // ─── Helper ───────────────────────────────────────────────────

    private void validateStatusTransition(OrderStatus from, OrderStatus to) {
        boolean valid = switch (from) {
            case CREATED    -> to == OrderStatus.IN_KITCHEN  || to == OrderStatus.CANCELLED;
            case IN_KITCHEN -> to == OrderStatus.PREPARING   || to == OrderStatus.CANCELLED;
            case PREPARING  -> to == OrderStatus.READY       || to == OrderStatus.CANCELLED;
            case READY      -> to == OrderStatus.SERVED      || to == OrderStatus.CANCELLED;
            case SERVED     -> to == OrderStatus.COMPLETED   || to == OrderStatus.CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
        if (!valid) {
            throw new IllegalStateException(
                    "Invalid status transition from " + from + " to " + to);
        }
    }
}