package com.example.crm.mappers;

import com.example.crm.entity.AppUser;
import com.example.crm.entity.Reservation;
import com.example.crm.entity.Table;
import com.example.crm.entity.dtos.request.ReservationRequestDTO;
import com.example.crm.entity.dtos.response.ReservationResponseDTO;
import com.example.crm.util.enums.ReservationStatus;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    /**
     * Maps ReservationRequestDTO to a new Reservation entity.
     * AppUser and RestaurantTable must be fetched in the service before calling this.
     */
    public Reservation toEntity(ReservationRequestDTO dto, AppUser customer, Table table) {
        Reservation reservation = new Reservation();
        reservation.setReservationDate(dto.getReservationDate());
        reservation.setReservationTime(dto.getReservationTime());
        reservation.setNumberOfPeople(dto.getNumberOfPeople());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCustomer(customer);
        reservation.setTable(table);
        return reservation;
    }

    /**
     * Updates an existing Reservation entity from ReservationRequestDTO.
     * AppUser and RestaurantTable must be fetched in the service before calling this.
     */
    public void updateEntity(Reservation reservation, ReservationRequestDTO dto,
                             AppUser customer, Table table) {
        reservation.setReservationDate(dto.getReservationDate());
        reservation.setReservationTime(dto.getReservationTime());
        reservation.setNumberOfPeople(dto.getNumberOfPeople());
        reservation.setCustomer(customer);
        reservation.setTable(table);
    }

    /**
     * Maps Reservation entity to ReservationResponseDTO.
     * Flattens customer and table details into the response.
     */
    public ReservationResponseDTO toResponse(Reservation reservation) {
        AppUser customer = reservation.getCustomer();
        Table table = reservation.getTable();

        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .numberOfPeople(reservation.getNumberOfPeople())
                .status(reservation.getStatus())
                // customer fields
                .customerId(customer.getId())
                .customerName(customer.getName())
                .customerEmail(customer.getEmail())
                .customerContact(customer.getContact())
                // table fields
                .tableId(table.getTableId())
                .tableNumber(table.getTableNumber())
                .tableCapacity(table.getCapacity())
                .tableStatus(table.getStatus())
                .build();
    }
}