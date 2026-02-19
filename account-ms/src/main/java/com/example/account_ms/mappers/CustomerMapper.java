package com.example.account_ms.mappers;

import com.example.account_ms.dto.input.CreateCustomerRequestDto;
import com.example.account_ms.dto.input.UpdateCustomerRequestDto;
import com.example.account_ms.dto.output.CustomerResponseDto;
import com.example.account_ms.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer fromDtoToEntity(CreateCustomerRequestDto dto) {
        return new Customer(
                null,
                dto.name(),
                dto.email(),
                dto.password()
        );
    }

    public CustomerResponseDto fromEntityToDto(Customer customer) {
        return new CustomerResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail()
        );
    }

    public void updateEntityFromDto(UpdateCustomerRequestDto dto, Customer customer) {
        customer.setName(dto.name());
        customer.setEmail(dto.email());
    }
}
