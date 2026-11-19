package org.carpooling.platform.dto.request;

public record VehicleRequestDTO(
        String make,
        String model,
        String registrationNumber,
        String color,
        int capacity,
        Long driverId
) {}

