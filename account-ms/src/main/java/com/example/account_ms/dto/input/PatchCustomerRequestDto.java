package com.example.account_ms.dto.input;

import java.time.LocalDate;

public record PatchCustomerRequestDto(
        String name,
        String email,
        LocalDate birthDate
) {}

