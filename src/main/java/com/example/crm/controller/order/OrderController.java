package com.example.crm.controller.order;

import com.example.crm.entity.dtos.request.order.OrderRequestDTO;
import com.example.crm.entity.dtos.request.order.OrderStatusUpdateRequestDTO;
import com.example.crm.entity.dtos.response.order.OrderResponseDTO;
import com.example.crm.service.OrderService;
import com.example.crm.util.enums.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(request));
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/get-order/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/get-order/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(
            @PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @GetMapping("/get-order/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomer(
            @PathVariable UUID customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    @GetMapping("/get-order/waiter/{waiterId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByWaiter(
            @PathVariable UUID waiterId) {
        return ResponseEntity.ok(orderService.getOrdersByWaiter(waiterId));
    }

    @GetMapping("/get-order/table/{tableId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByTable(
            @PathVariable UUID tableId) {
        return ResponseEntity.ok(orderService.getOrdersByTable(tableId));
    }

    @PatchMapping("/update-order/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderStatusUpdateRequestDTO request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request.getStatus()));
    }

    @DeleteMapping("/cancel-order/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}