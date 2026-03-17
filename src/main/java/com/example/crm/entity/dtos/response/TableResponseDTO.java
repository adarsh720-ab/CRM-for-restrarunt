package com.example.crm.entity.dtos.response;

import com.example.crm.util.enums.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableResponseDTO {

    private UUID tableId;
    private Integer tableNumber;
    private Integer capacity;
    private TableStatus status;
}
