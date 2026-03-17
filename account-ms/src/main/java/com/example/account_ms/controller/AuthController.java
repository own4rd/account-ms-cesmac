package com.example.account_ms.controller;

import com.example.account_ms.dto.input.CreateCustomerRequestDto;
import com.example.account_ms.dto.input.LoginRequestDto;
import com.example.account_ms.dto.output.TokenResponseDto;
import com.example.account_ms.mappers.CustomerMapper;
import com.example.account_ms.model.Customer;
import com.example.account_ms.repository.CustomerRepository;
import com.example.account_ms.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomerMapper customerMapper;

    public AuthController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
                          JwtService jwtService, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.customerMapper = customerMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@RequestBody @Valid CreateCustomerRequestDto dto) {
        Customer customer = customerMapper.fromDtoToEntity(dto);
        customer.setPassword(passwordEncoder.encode(dto.password()));
        customerRepository.save(customer);

        String token = jwtService.generateToken(customer.getEmail(), customer.getRole());
        return ResponseEntity.ok(new TokenResponseDto(token, "Bearer"));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        Customer customer = customerRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.password(), customer.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(customer.getEmail(), customer.getRole());
        return ResponseEntity.ok(new TokenResponseDto(token, "Bearer"));
    }
}
