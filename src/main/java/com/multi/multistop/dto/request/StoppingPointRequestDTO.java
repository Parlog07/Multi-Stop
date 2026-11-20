
package com.multi.multistop.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StoppingPointRequestDTO(

        @NotBlank(message = "City is required")
        String city,

        @Min(value = 1, message = "Stop order must start at 1")
        int pointOrder,

        @NotNull(message = "Passing time is required")
        @Future(message = "Passing time must be in the future")
        LocalDateTime passingTime,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", message = "Price cannot be negative")
        BigDecimal priceFromDeparture

) {}
