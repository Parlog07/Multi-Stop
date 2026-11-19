package com.multi.multistop.dto.request;


public record DriverRequestDTO(
        String lastName,
        String firstName,
        String email,
        String phone,
        String licenseNumber
) {}

