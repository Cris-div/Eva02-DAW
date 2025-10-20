package com.example.demo.repository;

import com.example.demo.entity.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosticoRepository  extends JpaRepository<Diagnostico, Long> {
}
