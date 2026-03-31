package com.example.crm.controller.orderitem;

import com.example.crm.entity.dtos.request.orderitem.OrderItemRequestDTO;
import com.example.crm.entity.dtos.response.orderitem.OrderItemResponseDTO;
import com.example.crm.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orderItem")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping("/add-item/{orderId}")
    public ResponseEntity<OrderItemResponseDTO> addItemToOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody OrderItemRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderItemService.addItemToOrder(orderId, request));
    }

    @GetMapping("/get-items/{orderId}")
    public ResponseEntity<List<OrderItemResponseDTO>> getItemsByOrderId(
            @PathVariable UUID orderId) {
        return ResponseEntity.ok(orderItemService.getItemsByOrderId(orderId));
    }


    @PutMapping("/update-item/{id}")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItem(
            @PathVariable UUID id,
            @Valid @RequestBody OrderItemRequestDTO request) {
        return ResponseEntity.ok(orderItemService.updateOrderItem(id, request));
    }

    @DeleteMapping("/remove-item/{id}")
    public ResponseEntity<Void> removeItemFromOrder(@PathVariable UUID id) {
        orderItemService.removeItemFromOrder(id);
        return ResponseEntity.noContent().build();
    }
}