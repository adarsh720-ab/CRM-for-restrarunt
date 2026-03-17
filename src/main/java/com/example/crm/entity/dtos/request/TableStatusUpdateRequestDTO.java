package com.example.crm.entity.dtos.request;

import com.example.crm.util.enums.TableStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableStatusUpdateRequestDTO {

    @NotNull(message = "Status is required")
    private TableStatus status;
}
