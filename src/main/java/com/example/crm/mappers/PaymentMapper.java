package com.example.crm.mappers;

import com.example.crm.entity.dtos.response.payment.PaymentResponseDTO;
import com.example.crm.entity.model.ordermodel.Order;
import com.example.crm.entity.model.payment.Payment;
import com.example.crm.util.enums.PaymentMethod;
import com.example.crm.util.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentMapper {

    /**
     * Creates a new Payment entity.
     * Order must be fetched in the service before calling this.
     */
    public Payment toEntity(Order order, PaymentMethod paymentMethod) {
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setOrder(order);
        return payment;
    }

    /**
     * Maps Payment entity to PaymentResponseDTO.
     * Calculates totalAmount from order items inline — no extra object allocation.
     */
    public PaymentResponseDTO toResponse(Payment payment) {
        Order order = payment.getOrder();

        double totalAmount = order.getOrderItems()
                .stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();

        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .paymentTime(payment.getPaymentTime())
                .orderId(order.getId())
                .customerName(order.getCustomer().getName())
                .totalAmount(totalAmount)
                .build();
    }
}