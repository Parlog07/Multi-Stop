package com.multi.multistop.dto.response;

import com.multi.multistop.enums.TripStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TripResponseDTO(
        Long id,
        String departureCity,
        String destinationCity,
        LocalDateTime departureDate,
        int seatsOffered,
        int remainingSeats,
        TripStatus status,
        Long driverId,
        Long vehicleId,
        List<StoppingPointResponseDTO> stoppingPoints
) {}

