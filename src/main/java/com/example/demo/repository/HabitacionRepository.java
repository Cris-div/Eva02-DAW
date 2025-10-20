package com.example.demo.repository;

import com.example.demo.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    // 🔹 Devuelve habitaciones disponibles (por estado)
    List<Habitacion> findByEstado(String estado);
}
