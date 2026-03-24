package com.example.account_ms.controller;

import com.example.account_ms.dto.input.CreateCustomerRequestDto;
import com.example.account_ms.dto.input.LoginRequestDto;
import com.example.account_ms.dto.output.ErrorResponseDto;
import com.example.account_ms.dto.output.TokenResponseDto;
import com.example.account_ms.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints de registro e login")
@SecurityRequirement(name = "")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar novo cliente", description = "Cria um novo cliente e retorna o token JWT")
    @ApiResponse(responseCode = "200", description = "Registro realizado com sucesso",
            content = @Content(schema = @Schema(implementation = TokenResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<TokenResponseDto> register(@RequestBody @Valid CreateCustomerRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar cliente", description = "Autentica o cliente e retorna o token JWT")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
            content = @Content(schema = @Schema(implementation = TokenResponseDto.class)))
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
