package com.example.crm.entity.dtos.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDTO {

    @NotNull(message = "Reservation date is required")
    @FutureOrPresent(message = "Reservation date must be today or in the future")
    private LocalDate reservationDate;

    @NotNull(message = "Reservation time is required")
    private LocalTime reservationTime;

    @NotNull(message = "Number of people is required")
    @Min(value = 1, message = "At least 1 person is required")
    private Integer numberOfPeople;

    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotNull(message = "Table ID is required")
    private UUID tableId;
}