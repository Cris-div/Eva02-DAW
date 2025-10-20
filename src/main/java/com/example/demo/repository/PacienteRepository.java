package com.example.demo.repository;

import com.example.demo.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // 🔹 Pacientes que están actualmente hospitalizados
    @Query("SELECT h.paciente FROM Hospitalizacion h WHERE h.estado = 'activo'")
    List<Paciente> findPacientesHospitalizados();

    // 🔹 Pacientes activos que NO están hospitalizados
    @Query("SELECT p FROM Paciente p WHERE p.estado = 'activo' AND p.idPaciente NOT IN " +
            "(SELECT h.paciente.idPaciente FROM Hospitalizacion h WHERE h.estado = 'activo')")
    List<Paciente> findPacientesNoHospitalizados();
}
