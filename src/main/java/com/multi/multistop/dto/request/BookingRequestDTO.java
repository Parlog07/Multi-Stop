
package com.multi.multistop.dto.request;

import jakarta.validation.constraints.*;

public record BookingRequestDTO(

        @NotNull(message = "Passenger ID is required")
        Long passengerId,

        @NotNull(message = "Trip ID is required")
        Long tripId,

        @NotNull(message = "Pickup point ID is required")
        Long pickUpPointId,

        @NotNull(message = "Drop-off point ID is required")
        Long dropOffPointId,

        @Min(value = 1, message = "At least 1 seat is required")
        int numberOfSeats

) {}
