package com.example.crm.repository;

import com.example.crm.entity.Reservation;
import com.example.crm.util.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByCustomer_Id(UUID customerId);

//    List<Reservation> findByTable_Id(UUID tableId);

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByReservationDate(LocalDate date);

//    List<Reservation> findByCustomer_IdAndStatus(UUID customerId, ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.table.tableId = :tableId " +
            "AND r.reservationDate = :date " +
            "AND r.reservationTime = :time " +
            "AND r.status NOT IN :excludedStatuses")
    List<Reservation> findConflictingReservations(
            @Param("tableId") UUID tableId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            @Param("excludedStatuses") List<ReservationStatus> excludedStatuses
    );
}