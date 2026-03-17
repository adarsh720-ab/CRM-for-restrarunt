package com.example.crm.controller.reservatoncontroller;

import com.example.crm.entity.dtos.request.ReservationRequestDTO;
import com.example.crm.entity.dtos.request.ReservationStatusUpdateRequestDTO;
import com.example.crm.entity.dtos.response.ReservationResponseDTO;
import com.example.crm.service.ReservationService;
import com.example.crm.util.enums.ReservationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/create-reservation")
    public ResponseEntity<ReservationResponseDTO> createReservation(@Valid @RequestBody ReservationRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(request));
    }

    @GetMapping("/get-all-reservations")
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations(){

        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/get-reservation/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @GetMapping("/get-reservation/customer/{customerId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByCustomer(
            @PathVariable UUID customerId) {
        return ResponseEntity.ok(reservationService.getReservationsByCustomer(customerId));
    }

    @GetMapping("/get-reservation/date/{date}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByDate(@PathVariable @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) LocalDate date){

        return ResponseEntity.ok(reservationService.getReservationsByDate(date));
    }

    @GetMapping("/get-reservation/status/{status}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByStatus(@PathVariable ReservationStatus status){

        return ResponseEntity.ok(reservationService.getReservationsByStatus(status));
    }

    @PutMapping("/update-reservation/{id}")
    public ResponseEntity<ReservationResponseDTO> updateReservation(@PathVariable UUID id, @Valid @RequestBody ReservationRequestDTO request){

        return ResponseEntity.ok(reservationService.updateReservation(id, request));
    }

    @PatchMapping("/update-reservation/{id}/status")
    public ResponseEntity<ReservationResponseDTO> updateReservationStatus(@PathVariable UUID id, @RequestBody ReservationStatusUpdateRequestDTO status){

       return ResponseEntity.ok(reservationService.updateReservationStatus(id, status.getStatus()));
    }

    @DeleteMapping("/cancel-reservation/{id}")
    public ResponseEntity<ReservationResponseDTO> cancelReservation(@PathVariable UUID id){

        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
