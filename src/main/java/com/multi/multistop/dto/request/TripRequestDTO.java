
package com.multi.multistop.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record TripRequestDTO(

        @NotNull(message = "Vehicle ID is required")
        Long vehicleId,

        @Min(value = 1, message = "At least 1 seat is required")
        int seatsOffered,

        @NotNull(message = "Stopping points are required")
        @Size(min = 2, message = "A trip needs at least 2 stops")
        List<@NotNull @Valid StoppingPointRequestDTO> stoppingPoints

) {}
