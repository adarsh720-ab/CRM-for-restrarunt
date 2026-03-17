package com.example.crm.entity;

import com.example.crm.util.enums.TableStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@jakarta.persistence.Table(name = "Tables")
public class Table {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID tableId;

    @Column(nullable = false, unique = true)
    private Integer tableNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private TableStatus status;
}
