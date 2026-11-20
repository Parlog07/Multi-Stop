
package com.multi.multistop.controller;

import com.multi.multistop.dto.request.TripRequestDTO;
import com.multi.multistop.dto.response.TripResponseDTO;
import com.multi.multistop.service.TripService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripResponseDTO> create(
            @Valid @RequestBody TripRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tripService.create(request));
    }

    @GetMapping
    public List<TripResponseDTO> getAll() {
        return tripService.getAll();
    }

    @GetMapping("/{id}")
    public TripResponseDTO getById(@PathVariable Long id) {
        return tripService.getById(id);
    }

    @GetMapping("/driver/{driverId}")
    public List<TripResponseDTO> getByDriverId(
            @PathVariable Long driverId) {

        return tripService.getByDriverId(driverId);
    }

    @GetMapping("/search")
    public Page<TripResponseDTO> searchTrips(
            @RequestParam String departure,
            @RequestParam String destination,
            @RequestParam(defaultValue = "1") int seats,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return tripService.searchTrips(
                departure,
                destination,
                seats,
                page,
                size
        );
    }

}
