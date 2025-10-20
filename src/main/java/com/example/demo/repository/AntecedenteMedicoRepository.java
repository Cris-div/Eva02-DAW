package com.example.demo.repository;

import com.example.demo.entity.AntecedenteMedico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AntecedenteMedicoRepository extends JpaRepository<AntecedenteMedico, Long> {
}
