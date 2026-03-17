package com.example.crm.controller.payment;

import com.example.crm.entity.dtos.request.payment.PaymentRequestDTO;
import com.example.crm.entity.dtos.request.payment.PaymentStatusUpdateRequestDTO;
import com.example.crm.entity.dtos.response.payment.PaymentResponseDTO;
import com.example.crm.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment")
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @Valid @RequestBody PaymentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.createPayment(request));
    }

    @GetMapping("/get-all-payments")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/get-payment/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/get-payment/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(
            @PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @PutMapping("/update-payment/{id}")
    public ResponseEntity<PaymentResponseDTO> updatePayment(
            @PathVariable UUID id,
            @Valid @RequestBody PaymentRequestDTO request) {
        return ResponseEntity.ok(paymentService.updatePayment(id, request));
    }

    @PatchMapping("/update-payment/{id}/status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @PathVariable UUID id,
            @Valid @RequestBody PaymentStatusUpdateRequestDTO request) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, request));
    }

    @DeleteMapping("/delete-payment/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable UUID id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}