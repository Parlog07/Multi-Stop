
package com.multi.multistop.controller;

import com.multi.multistop.dto.request.BookingRequestDTO;
import com.multi.multistop.dto.response.BookingResponseDTO;
import com.multi.multistop.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(
            @Valid @RequestBody BookingRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.create(request));
    }

    @GetMapping
    public List<BookingResponseDTO> getAll() {
        return bookingService.getAll();
    }

    @GetMapping("/{id}")
    public BookingResponseDTO getById(@PathVariable Long id) {
        return bookingService.getById(id);
    }

    @GetMapping("/passenger/{passengerId}")
    public List<BookingResponseDTO> getByPassengerId(
            @PathVariable Long passengerId) {

        return bookingService.getByPassengerId(passengerId);
    }

    @GetMapping("/trip/{tripId}")
    public List<BookingResponseDTO> getByTripId(
            @PathVariable Long tripId) {

        return bookingService.getByTripId(tripId);
    }

    @PatchMapping("/{id}/cancel")
    public BookingResponseDTO cancel(@PathVariable Long id) {
        return bookingService.cancel(id);
    }
}
