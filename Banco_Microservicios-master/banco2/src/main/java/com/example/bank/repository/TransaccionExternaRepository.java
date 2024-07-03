package com.example.bank.repository;

import com.example.bank.model.entity.TransaccionExterna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionExternaRepository extends JpaRepository<TransaccionExterna, Long> {
}
