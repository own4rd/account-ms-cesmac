package com.example.account_ms.service;

import com.example.account_ms.dto.input.CreateCustomerRequestDto;
import com.example.account_ms.dto.input.LoginRequestDto;
import com.example.account_ms.dto.output.TokenResponseDto;
import com.example.account_ms.mappers.CustomerMapper;
import com.example.account_ms.model.Customer;
import com.example.account_ms.repository.CustomerRepository;
import com.example.account_ms.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomerMapper customerMapper;

    public AuthService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.customerMapper = customerMapper;
    }

    public TokenResponseDto register(CreateCustomerRequestDto dto) {
        Customer customer = customerMapper.fromDtoToEntity(dto);
        customer.setPassword(passwordEncoder.encode(dto.password()));
        customerRepository.save(customer);

        String token = jwtService.generateToken(customer.getEmail(), customer.getRole());
        return new TokenResponseDto(token, "Bearer");
    }

    public TokenResponseDto login(LoginRequestDto dto) {
        Customer customer = customerRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.password(), customer.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(customer.getEmail(), customer.getRole());
        return new TokenResponseDto(token, "Bearer");
    }
}
