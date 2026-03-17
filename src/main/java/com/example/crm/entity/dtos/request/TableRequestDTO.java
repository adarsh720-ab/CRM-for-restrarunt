package com.example.crm.entity.dtos.request;

import com.example.crm.util.enums.TableStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableRequestDTO {

    @NotNull(message = "Table number is required")
    @Positive(message = "Table number must be a positive number")
    private Integer tableNumber;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;


    private TableStatus status;
}
