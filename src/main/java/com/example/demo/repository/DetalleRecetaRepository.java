package com.example.demo.repository;

import com.example.demo.entity.DetalleReceta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleRecetaRepository extends JpaRepository<DetalleReceta, Long> {
}
