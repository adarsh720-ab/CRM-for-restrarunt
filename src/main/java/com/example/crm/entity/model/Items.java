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
@Table(name = "items")
public class Items {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(name = "item_id", updatable = false, nullable = false)
    private UUID itemId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Float itemPrice;

    private String itemDescription;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;

    private Boolean available = true;
}