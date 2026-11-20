
package com.multi.multistop.controller;

import com.multi.multistop.dto.request.DriverRequestDTO;
import com.multi.multistop.dto.response.DriverResponseDTO;
import com.multi.multistop.service.DriverService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverResponseDTO> create(
            @Valid @RequestBody DriverRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(driverService.create(request));
    }

    @GetMapping
    public List<DriverResponseDTO> getAll() {
        return driverService.getAll();
    }

    @GetMapping("/{id}")
    public DriverResponseDTO getById(@PathVariable Long id) {
        return driverService.getById(id);
    }

    @PutMapping("/{id}")
    public DriverResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody DriverRequestDTO request) {

        return driverService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        driverService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
