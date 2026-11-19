package com.multi.multistop.dto.response;

public record DriverResponseDTO(
        Long id,
        String lastName,
        String firstName,
        String email,
        String phone,
        String licenseNumber
) {}

