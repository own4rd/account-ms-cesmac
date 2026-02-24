package com.example.account_ms.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateDependentRequestDto(
        @NotBlank
        String name,

        LocalDate birthDate,

        @NotBlank
        String relationship
) {
}
