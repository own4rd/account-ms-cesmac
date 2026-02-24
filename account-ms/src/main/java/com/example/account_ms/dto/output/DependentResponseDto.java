package com.example.account_ms.dto.output;

import java.time.LocalDate;
import java.util.UUID;

public record DependentResponseDto(
        UUID id,
        String name,
        LocalDate birthDate,
        String relationship
) {
}
