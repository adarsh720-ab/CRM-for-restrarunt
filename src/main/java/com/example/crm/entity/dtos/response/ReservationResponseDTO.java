package com.example.crm.entity.dtos.response;

import com.example.crm.util.enums.ReservationStatus;
import com.example.crm.util.enums.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDTO {

    private UUID id;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private Integer numberOfPeople;
    private ReservationStatus status;

    // Customer summary
    private UUID customerId;
    private String customerName;
    private String customerEmail;
    private String customerContact;

    // Table summary
    private UUID tableId;
    private Integer tableNumber;
    private Integer tableCapacity;
    private TableStatus tableStatus;
}