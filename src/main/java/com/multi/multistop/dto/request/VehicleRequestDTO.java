
package com.multi.multistop.dto.request;

import jakarta.validation.constraints.*;

public record VehicleRequestDTO(

        @NotBlank(message = "Make is required")
        String make,

        @NotBlank(message = "Model is required")
        String model,

        @NotBlank(message = "Registration number is required")
        String registrationNumber,

        @NotBlank(message = "Color is required")
        String color,

        @Min(value = 1, message = "Capacity must be at least 1")
        @Max(value = 8, message = "Capacity cannot exceed 8")
        int capacity,

        @NotNull(message = "Driver ID is required")
        Long driverId

) {}
