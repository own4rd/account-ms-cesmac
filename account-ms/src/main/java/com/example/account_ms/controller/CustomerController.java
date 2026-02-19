package com.example.account_ms.controller;

import com.example.account_ms.dto.input.CreateCustomerRequestDto;
import com.example.account_ms.dto.input.PatchCustomerRequestDto;
import com.example.account_ms.dto.input.UpdateCustomerRequestDto;
import com.example.account_ms.dto.output.CustomerResponseDto;
import com.example.account_ms.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CreateCustomerRequestDto customerDto) {
        customerService.save(customerDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> list() {
        var customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable UUID uuid) {
        var customer = customerService.findById(uuid);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<CustomerResponseDto> update(
            @PathVariable UUID uuid,
            @RequestBody @Valid UpdateCustomerRequestDto dto
    ) {
        var updated = customerService.update(uuid, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<CustomerResponseDto> patch(
            @PathVariable UUID uuid,
            @RequestBody PatchCustomerRequestDto dto
    ) {
        var updated = customerService.patch(uuid, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        customerService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}
