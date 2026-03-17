package com.example.crm.service.impl;

import com.example.crm.entity.AppUser;
import com.example.crm.entity.Reservation;
import com.example.crm.entity.Table;
import com.example.crm.entity.dtos.request.ReservationRequestDTO;
import com.example.crm.entity.dtos.response.ReservationResponseDTO;
import com.example.crm.mappers.ReservationMapper;
import com.example.crm.repository.AppUserRepository;
import com.example.crm.repository.ReservationRepository;
import com.example.crm.repository.RestaurantTableRepository;
import com.example.crm.service.ReservationService;
import com.example.crm.util.enums.ReservationStatus;
import com.example.crm.util.enums.TableStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository tableRepository;
    private final AppUserRepository appUserRepository;
    private final ReservationMapper reservationMapper;
    @Override
    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO request) {

        AppUser customer = appUserRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with id: " + request.getCustomerId()));

        Table table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No table found with id: " + request.getTableId()));

        if (table.getCapacity() < request.getNumberOfPeople()) {
            throw new IllegalArgumentException(
                    "Table capacity (" + table.getCapacity() + ") is less than number of people ("
                            + request.getNumberOfPeople() + ")");
        }

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                table.getTableId(),
                request.getReservationDate(),
                request.getReservationTime(),
                List.of(ReservationStatus.CANCELED, ReservationStatus.COMPLETE) // ← added
        );

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException(
                    "Table is already reserved at the requested date and time");
        }

        Reservation reservation = reservationMapper.toEntity(request, customer, table);
        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(reservationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO getReservationById(UUID id) {
        return reservationRepository.findById(id)
                .map(reservationMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Reservation not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByCustomer(UUID customerId) {

        return reservationRepository.findByCustomer_Id(customerId)
                .stream()
                .map(reservationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByDate(LocalDate date) {
        return reservationRepository.findByReservationDate(date)
                .stream()
                .map(reservationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status)
                .stream()
                .map(reservationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReservationResponseDTO updateReservation(UUID id, ReservationRequestDTO request) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No reservation found with "+ id));

        if (reservation.getStatus() == ReservationStatus.COMPLETE || reservation.getStatus() == ReservationStatus.COMPLETE){
            throw new IllegalStateException("Cannot update the Reservation.");
        }

        AppUser customer = appUserRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with id: " + request.getCustomerId()));

        Table newTable = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Table not found with id: " + request.getTableId()));

        if (newTable.getCapacity() < request.getNumberOfPeople()) {
            throw new IllegalArgumentException(
                    "Table capacity is insufficient for the party size");
        }

        // if table changed, release old table and reserve new one
        if (!reservation.getTable().getTableId().equals(newTable.getTableId())) {
            reservation.getTable().setStatus(TableStatus.AVAILABLE);
            tableRepository.save(reservation.getTable());
            newTable.setStatus(TableStatus.RESERVED);
            tableRepository.save(newTable);
        }

        reservationMapper.updateEntity(reservation, request, customer, newTable);
        Reservation updated = reservationRepository.save(reservation);

        return reservationMapper.toResponse(updated);

    }

    @Override
    @Transactional
    public ReservationResponseDTO updateReservationStatus(UUID id, ReservationStatus Status) {


        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Reservation not found with id: " + id));

        validateStatusTransition(reservation.getStatus(), Status);
        reservation.setStatus(Status);

        // sync table status based on reservation status
        Table table = reservation.getTable();
        switch (Status) {
            case CONFIRMED -> table.setStatus(TableStatus.RESERVED);
            case CANCELED  -> table.setStatus(TableStatus.AVAILABLE);
            case COMPLETE  -> table.setStatus(TableStatus.CLEANING);
            default -> { /* PENDING: no table change */ }
        }
        tableRepository.save(table);

        Reservation updated = reservationRepository.save(reservation);
        return reservationMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void cancelReservation(UUID id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Reservation not found with id : "+ id));

        if (reservation.getStatus() == ReservationStatus.COMPLETE){
            throw new IllegalStateException("Cannot cancel a completed reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        reservation.getTable().setStatus(TableStatus.AVAILABLE);
        tableRepository.save(reservation.getTable());
        reservationRepository.save(reservation);
    }

    private void validateStatusTransition(ReservationStatus from, ReservationStatus to) {
        boolean valid = switch (from) {
            case PENDING   -> to == ReservationStatus.CONFIRMED || to == ReservationStatus.CANCELED;
            case CONFIRMED -> to == ReservationStatus.COMPLETE  || to == ReservationStatus.CANCELED;
            case CANCELED, COMPLETE -> false;
        };
        if (!valid) {
            throw new IllegalStateException(
                    "Invalid status transition from " + from + " to " + to);
        }
    }
}
