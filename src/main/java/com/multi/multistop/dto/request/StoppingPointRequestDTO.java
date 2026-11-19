package com.multi.multistop.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StoppingPointRequestDTO(
        String city,
        int pointOrder,
        LocalDateTime passingTime,
        BigDecimal priceFromDeparture
) {}

