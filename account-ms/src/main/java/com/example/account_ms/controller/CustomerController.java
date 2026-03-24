package com.example.account_ms.controller;

import com.example.account_ms.dto.input.PatchCustomerRequestDto;
import com.example.account_ms.dto.input.UpdateCustomerRequestDto;
import com.example.account_ms.dto.output.CustomerResponseDto;
import com.example.account_ms.dto.output.ErrorResponseDto;
import com.example.account_ms.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Clientes", description = "Operações de gerenciamento de clientes")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Retorna todos os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomerResponseDto.class))))
    public ResponseEntity<List<CustomerResponseDto>> list() {
        var customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente pelo seu UUID")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado",
            content = @Content(schema = @Schema(implementation = CustomerResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<CustomerResponseDto> findById(
            @Parameter(description = "UUID do cliente") @PathVariable UUID uuid) {
        var customer = customerService.findById(uuid);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza todos os dados de um cliente")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = CustomerResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<CustomerResponseDto> update(
            @Parameter(description = "UUID do cliente") @PathVariable UUID uuid,
            @RequestBody @Valid UpdateCustomerRequestDto dto
    ) {
        var updated = customerService.update(uuid, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{uuid}")
    @Operation(summary = "Atualizar parcialmente um cliente", description = "Atualiza apenas os campos informados")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = CustomerResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<CustomerResponseDto> patch(
            @Parameter(description = "UUID do cliente") @PathVariable UUID uuid,
            @RequestBody PatchCustomerRequestDto dto
    ) {
        var updated = customerService.patch(uuid, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Remover cliente", description = "Remove um cliente pelo seu UUID")
    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID do cliente") @PathVariable UUID uuid) {
        customerService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}
