package com.example.crm.repository;

import com.example.crm.entity.model.ordermodel.Order;
import com.example.crm.util.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByOrderStatus(OrderStatus status);

    List<Order> findByCustomer_Id(UUID customerId);

    List<Order> findByWaiter_Id(UUID waiterId);

    @Query("select o from Order o where o.table.tableId = :tableId")
    List<Order> findByTableId(@Param("tableId") UUID tableId);
}