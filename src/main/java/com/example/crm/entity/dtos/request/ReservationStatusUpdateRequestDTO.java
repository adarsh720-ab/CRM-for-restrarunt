package com.example.crm.entity.dtos.request;

import com.example.crm.util.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStatusUpdateRequestDTO {

    @NotNull(message = "Status is required.")
    private ReservationStatus status;
}
