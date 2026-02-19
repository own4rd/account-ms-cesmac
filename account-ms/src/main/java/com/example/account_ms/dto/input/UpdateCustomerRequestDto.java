package com.example.account_ms.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerRequestDto(

        @NotBlank
        String name,

        @NotBlank
        @Email
        String email
) {}
