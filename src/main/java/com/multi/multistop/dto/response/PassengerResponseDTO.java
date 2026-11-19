package com.multi.multistop.dto.response;

public record PassengerResponseDTO(
        Long id,
        String lastName,
        String firstName,
        String email,
        String phone
) {}

