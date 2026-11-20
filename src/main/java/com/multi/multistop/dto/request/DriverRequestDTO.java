
package com.multi.multistop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DriverRequestDTO(

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone is required")
        String phone,

        @NotBlank(message = "License number is required")
        String licenseNumber

) {}
