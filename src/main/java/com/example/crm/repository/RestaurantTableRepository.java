package com.example.crm.repository;

import com.example.crm.entity.Table;
import com.example.crm.util.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantTableRepository extends JpaRepository<Table, UUID> {

    List<Table> findByStatus(TableStatus status);

    Optional<Table> findByTableNumber(Integer tableNumber);

    boolean existsByTableNumber(Integer tableNumber);

    List<Table> findByCapacityGreaterThanEqual(Integer capacity);
}