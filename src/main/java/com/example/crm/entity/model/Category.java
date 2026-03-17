package com.example.crm.entity.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(name = "category_id", updatable = false, nullable = false)
    private UUID categoryId;

    @Column(nullable = false)
    private String categoryName;

    @Column
    private String description;
}