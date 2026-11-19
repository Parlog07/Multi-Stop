package com.multi.multistop.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StoppingPointResponseDTO(
        Long id,
        String city,
        int pointOrder,
        LocalDateTime passingTime,
        BigDecimal priceFromDeparture
) {}

