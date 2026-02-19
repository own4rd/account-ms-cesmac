package com.example.account_ms.dto.output;


import java.util.UUID;

public record CustomerResponseDto(
        UUID id,
        String name,
        String email
) {
}