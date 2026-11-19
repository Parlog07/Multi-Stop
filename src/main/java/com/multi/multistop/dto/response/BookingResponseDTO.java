package com.multi.multistop.dto.response;

import com.multi.multistop.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponseDTO(
        Long id,
        Long passengerId,
        Long tripId,
        Long pickUpPointId,
        Long dropOffPointId,
        int numberOfSeats,
        BigDecimal totalAmount,
        LocalDateTime bookingDate,
        BookingStatus status
) {}

