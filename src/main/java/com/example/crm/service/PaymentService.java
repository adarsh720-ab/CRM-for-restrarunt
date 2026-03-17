package com.example.crm.service;

import com.example.crm.entity.dtos.request.payment.PaymentRequestDTO;
import com.example.crm.entity.dtos.request.payment.PaymentStatusUpdateRequestDTO;
import com.example.crm.entity.dtos.response.payment.PaymentResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PaymentResponseDTO createPayment(PaymentRequestDTO request);

    PaymentResponseDTO getPaymentById(UUID id);

    PaymentResponseDTO getPaymentByOrderId(UUID orderId);

    List<PaymentResponseDTO> getAllPayments();

    PaymentResponseDTO updatePayment(UUID id, PaymentRequestDTO request);

    PaymentResponseDTO updatePaymentStatus(UUID id, PaymentStatusUpdateRequestDTO request);

    void deletePayment(UUID id);
}