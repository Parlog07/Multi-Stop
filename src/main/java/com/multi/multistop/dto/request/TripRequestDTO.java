package org.carpooling.platform.dto.request;


import java.util.List;

public record TripRequestDTO(
        Long vehicleId,
        int seatsOffered,
        List<StoppingPointRequestDTO> stoppingPoints
) {}

