package com.example.account_ms.service;

import com.example.account_ms.dto.input.CreateCustomerRequestDto;
import com.example.account_ms.dto.input.PatchCustomerRequestDto;
import com.example.account_ms.dto.input.UpdateCustomerRequestDto;
import com.example.account_ms.dto.output.CustomerResponseDto;
import com.example.account_ms.exceptions.ResourceNotFoundException;
import com.example.account_ms.mappers.CustomerMapper;
import com.example.account_ms.model.Customer;
import com.example.account_ms.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public void save(CreateCustomerRequestDto customerDto) {
        var customer = customerMapper.fromDtoToEntity(customerDto);
        customerRepository.save(customer);
    }

    public List<CustomerResponseDto> findAll() {
        return customerRepository.findAll().stream().map(customerMapper::fromEntityToDto).toList();
    }

    public CustomerResponseDto findById(UUID uuid) {
        Customer customer = customerRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("ID inválido!"));

        return customerMapper.fromEntityToDto(customer);
    }

    public CustomerResponseDto update(UUID id, UpdateCustomerRequestDto dto) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID inválido!"));

        customerMapper.updateEntityFromDto(dto, customer);

        customerRepository.save(customer);

        return customerMapper.fromEntityToDto(customer);
    }

    public CustomerResponseDto patch(UUID id, PatchCustomerRequestDto dto) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID inválido!"));

        if (dto.name() != null) {
            customer.setName(dto.name());
        }

        if (dto.email() != null) {
            customer.setEmail(dto.email());
        }

        customerRepository.save(customer);

        return customerMapper.fromEntityToDto(customer);
    }

    public void delete(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer com ID " + id + " não encontrado.");
        }

        customerRepository.deleteById(id);
    }

}
