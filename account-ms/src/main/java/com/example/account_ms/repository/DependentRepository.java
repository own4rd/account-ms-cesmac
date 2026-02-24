package com.example.account_ms.repository;

import com.example.account_ms.model.Dependent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DependentRepository extends JpaRepository<Dependent, UUID> {
}
