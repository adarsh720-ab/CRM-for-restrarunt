package com.example.crm.service.impl;

import com.example.crm.entity.dtos.request.payment.PaymentRequestDTO;
import com.example.crm.entity.dtos.request.payment.PaymentStatusUpdateRequestDTO;
import com.example.crm.entity.dtos.response.payment.PaymentResponseDTO;
import com.example.crm.entity.model.ordermodel.Order;
import com.example.crm.entity.model.payment.Payment;
import com.example.crm.mappers.PaymentMapper;
import com.example.crm.repository.OrderRepository;
import com.example.crm.repository.PaymentRepository;
import com.example.crm.service.PaymentService;
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
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    // ─── Create ───────────────────────────────────────────────────

    @Override
    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO request) {
        log.info("Creating payment for orderId: {}", request.getOrderId());

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + request.getOrderId()));

        if (order.getOrderStatus() != OrderStatus.SERVED
                && order.getOrderStatus() != OrderStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Payment can only be created for SERVED or COMPLETED orders");
        }

        if (paymentRepository.existsByOrder_Id(request.getOrderId())) {
            throw new IllegalStateException(
                    "Payment already exists for orderId: " + request.getOrderId());
        }

        Payment saved = paymentRepository.save(
                paymentMapper.toEntity(order, request.getPaymentMethod()));

        log.info("Payment created successfully with id: {}", saved.getId());
        return paymentMapper.toResponse(saved);
    }

    // ─── Read ─────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentById(UUID id) {
        log.info("Fetching payment with id: {}", id);
        return paymentRepository.findById(id)
                .map(paymentMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Payment not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentByOrderId(UUID orderId) {
        log.info("Fetching payment for orderId: {}", orderId);
        return paymentRepository.findByOrder_Id(orderId)
                .map(paymentMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Payment not found for orderId: " + orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getAllPayments() {
        log.info("Fetching all payments");
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ─── Update ───────────────────────────────────────────────────

    @Override
    @Transactional
    public PaymentResponseDTO updatePayment(UUID id, PaymentRequestDTO request) {
        log.info("Updating payment with id: {}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Payment not found with id: " + id));

        if (payment.getPaymentStatus() == com.example.crm.util.enums.PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Cannot update a successful payment");
        }

        payment.setPaymentMethod(request.getPaymentMethod());

        Payment updated = paymentRepository.save(payment);
        log.info("Payment updated successfully with id: {}", updated.getId());
        return paymentMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public PaymentResponseDTO updatePaymentStatus(UUID id, PaymentStatusUpdateRequestDTO request) {
        log.info("Updating payment status for id: {} to {}", id, request.getPaymentStatus());

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Payment not found with id: " + id));

        if (payment.getPaymentStatus() == com.example.crm.util.enums.PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Cannot update status of a successful payment");
        }

        payment.setPaymentStatus(request.getPaymentStatus());

        Payment updated = paymentRepository.save(payment);
        log.info("Payment status updated to {} for id: {}", request.getPaymentStatus(), id);
        return paymentMapper.toResponse(updated);
    }

    // ─── Delete ───────────────────────────────────────────────────

    @Override
    @Transactional
    public void deletePayment(UUID id) {
        log.info("Deleting payment with id: {}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Payment not found with id: " + id));

        if (payment.getPaymentStatus() == com.example.crm.util.enums.PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Cannot delete a successful payment");
        }

        paymentRepository.delete(payment);
        log.info("Payment deleted successfully with id: {}", id);
    }
}