
package com.multi.multistop.controller;

import com.multi.multistop.dto.request.PassengerRequestDTO;
import com.multi.multistop.dto.response.PassengerResponseDTO;
import com.multi.multistop.service.PassengerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping
    public ResponseEntity<PassengerResponseDTO> create(
            @Valid @RequestBody PassengerRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(passengerService.create(request));
    }

    @GetMapping
    public List<PassengerResponseDTO> getAll() {
        return passengerService.getAll();
    }

    @GetMapping("/{id}")
    public PassengerResponseDTO getById(@PathVariable Long id) {
        return passengerService.getById(id);
    }

    @PutMapping("/{id}")
    public PassengerResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody PassengerRequestDTO request) {

        return passengerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        passengerService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
