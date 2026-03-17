package com.example.crm.service;

import com.example.crm.entity.dtos.request.ReservationRequestDTO;
import com.example.crm.entity.dtos.response.ReservationResponseDTO;
import com.example.crm.util.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationService {

    ReservationResponseDTO createReservation(ReservationRequestDTO request);

    List<ReservationResponseDTO> getAllReservations();

    ReservationResponseDTO getReservationById(UUID id);

    List<ReservationResponseDTO> getReservationsByCustomer(UUID customerId);

    List<ReservationResponseDTO> getReservationsByDate(LocalDate date);

    List<ReservationResponseDTO> getReservationsByStatus(ReservationStatus status);

    ReservationResponseDTO updateReservation(UUID id, ReservationRequestDTO request);

    ReservationResponseDTO updateReservationStatus(UUID id, ReservationStatus status);

    void cancelReservation(UUID id);
}