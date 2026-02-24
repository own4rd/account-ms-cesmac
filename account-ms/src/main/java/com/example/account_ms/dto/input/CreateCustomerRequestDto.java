package com.example.account_ms.dto.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateCustomerRequestDto(
        @NotNull
        @NotEmpty
        String name,
        @NotNull
        @NotEmpty
        String email,
        @NotNull
        @NotEmpty
        String password,
        LocalDate birthDate

) {
}
