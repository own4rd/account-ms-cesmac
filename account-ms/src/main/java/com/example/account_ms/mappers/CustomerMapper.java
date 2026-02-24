package com.example.account_ms.mappers;

import com.example.account_ms.dto.input.CreateCustomerRequestDto;
import com.example.account_ms.dto.input.CreateDependentRequestDto;
import com.example.account_ms.dto.input.UpdateCustomerRequestDto;
import com.example.account_ms.dto.output.CustomerResponseDto;
import com.example.account_ms.dto.output.DependentResponseDto;
import com.example.account_ms.model.Customer;
import com.example.account_ms.model.Dependent;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CustomerMapper {

//    TODO Pesquisem sobre MapStruct
    public Customer fromDtoToEntity(CreateCustomerRequestDto dto) {
        Customer customer = new Customer(
                dto.name(),
                dto.email(),
                dto.password()
        );
        customer.setBirthDate(dto.birthDate());

        return customer;
    }

    public CustomerResponseDto fromEntityToDto(Customer customer) {
        List<DependentResponseDto> dependents = customer.getDependents() != null
                ? customer.getDependents().stream().map(this::fromDependentEntityToDto).toList()
                : Collections.emptyList();

        return new CustomerResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getBirthDate(),
                customer.getAge(),
                dependents
        );
    }

    public void updateEntityFromDto(UpdateCustomerRequestDto dto, Customer customer) {
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setBirthDate(dto.birthDate());
    }

    public Dependent fromDependentDtoToEntity(CreateDependentRequestDto dto) {
        return new Dependent(dto.name(), dto.birthDate(), dto.relationship());
    }

    public DependentResponseDto fromDependentEntityToDto(Dependent dependent) {
        return new DependentResponseDto(
                dependent.getId(),
                dependent.getName(),
                dependent.getBirthDate(),
                dependent.getRelationship()
        );
    }
}
