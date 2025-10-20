package com.example.demo.repository;

import com.example.demo.entity.Hospitalizacion;
import com.example.demo.entity.Habitacion;
import com.example.demo.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface HospitalizacionRepository extends JpaRepository<Hospitalizacion, Long> {

    // 🔹 Devuelve las habitaciones que están actualmente ocupadas
    @Query("SELECT h.habitacion FROM Hospitalizacion h WHERE h.estado = 'activo'")
    List<Habitacion> findHabitacionesOcupadas();

    // 🔹 Devuelve los pacientes que están hospitalizados
    @Query("SELECT h.paciente FROM Hospitalizacion h WHERE h.estado = 'activo'")
    List<Paciente> findPacientesHospitalizados();

    // 🔹 Devuelve todas las hospitalizaciones activas
    List<Hospitalizacion> findByEstado(String estado);

    boolean existsByPacienteIdPacienteAndEstado(Long idPaciente, String estado);
}
