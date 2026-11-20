
package com.multi.multistop.controller;

import com.multi.multistop.dto.request.VehicleRequestDTO;
import com.multi.multistop.dto.response.VehicleResponseDTO;
import com.multi.multistop.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> create(
            @Valid @RequestBody VehicleRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehicleService.create(request));
    }

    @GetMapping
    public List<VehicleResponseDTO> getAll() {
        return vehicleService.getAll();
    }

    @GetMapping("/{id}")
    public VehicleResponseDTO getById(@PathVariable Long id) {
        return vehicleService.getById(id);
    }

    @GetMapping("/driver/{driverId}")
    public List<VehicleResponseDTO> getByDriverId(@PathVariable Long driverId) {
        return vehicleService.getByDriverId(driverId);
    }

    @PutMapping("/{id}")
    public VehicleResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequestDTO request) {

        return vehicleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
