package com.example.demo.controller;

import com.example.demo.entity.Hospitalizacion;
import com.example.demo.entity.Paciente;
import com.example.demo.entity.Habitacion;
import com.example.demo.repository.HospitalizacionRepository;
import com.example.demo.repository.PacienteRepository;
import com.example.demo.repository.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hospitalizaciones")
@CrossOrigin(origins = "*")
public class HospitalizacionController {

    @Autowired
    private HospitalizacionRepository hospRepo;

    @Autowired
    private PacienteRepository pacRepo;

    @Autowired
    private HabitacionRepository habRepo;

    // DTO para mapear el id correctamente
    public static class HospitalizacionDTO {
        public Long idHospitalizacion;
        public Paciente paciente;
        public Habitacion habitacion;
        public String diagnosticoIngreso;
        public LocalDate fechaIngreso;
        public LocalDate fechaAlta;
        public String estado;

        public HospitalizacionDTO(Hospitalizacion h) {
            this.idHospitalizacion = h.getIdHosp(); // aquí mapeamos idHosp → idHospitalizacion
            this.paciente = h.getPaciente();
            this.habitacion = h.getHabitacion();
            this.diagnosticoIngreso = h.getDiagnosticoIngreso();
            this.fechaIngreso = h.getFechaIngreso();
            this.fechaAlta = h.getFechaAlta();
            this.estado = h.getEstado();
        }
    }

    // 🔹 1. Listar todas las hospitalizaciones
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HospitalizacionDTO>> listar() {
        List<HospitalizacionDTO> lista = hospRepo.findAll()
                .stream()
                .map(HospitalizacionDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // 🔹 2. Buscar una hospitalización por ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HospitalizacionDTO> obtenerPorId(@PathVariable Long id) {
        return hospRepo.findById(id)
                .map(HospitalizacionDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 3. Crear nueva hospitalización
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> crear(@RequestBody Hospitalizacion hospData) {
        try {
            if (hospData.getPaciente() == null || hospData.getHabitacion() == null) {
                return ResponseEntity.badRequest().body("⚠️ Debes enviar paciente y habitación");
            }

            Long idPaciente = hospData.getPaciente().getIdPaciente();
            Long idHabitacion = hospData.getHabitacion().getIdHabitacion();

            Paciente paciente = pacRepo.findById(idPaciente).orElse(null);
            Habitacion habitacion = habRepo.findById(idHabitacion).orElse(null);

            if (paciente == null) return ResponseEntity.badRequest().body("⚠️ Paciente no encontrado");
            if (habitacion == null) return ResponseEntity.badRequest().body("⚠️ Habitación no encontrada");
            if (!"disponible".equalsIgnoreCase(habitacion.getEstado()))
                return ResponseEntity.badRequest().body("⚠️ La habitación no está disponible");

            boolean yaHospitalizado = hospRepo.existsByPacienteIdPacienteAndEstado(idPaciente, "activo");
            if (yaHospitalizado)
                return ResponseEntity.badRequest().body("⚠️ El paciente ya tiene una hospitalización activa");

            hospData.setPaciente(paciente);
            hospData.setHabitacion(habitacion);
            hospData.setFechaIngreso(LocalDate.now());
            hospData.setEstado("activo");

            habitacion.setEstado("ocupada");
            habRepo.save(habitacion);

            Hospitalizacion nueva = hospRepo.save(hospData);
            return ResponseEntity.ok(new HospitalizacionDTO(nueva));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("❌ Error al registrar hospitalización: " + e.getMessage());
        }
    }

    // 🔹 4. Dar de alta
    @PutMapping("/{id}/alta")
    public ResponseEntity<?> darAlta(@PathVariable Long id) {
        Hospitalizacion hosp = hospRepo.findById(id).orElse(null);
        if (hosp == null) return ResponseEntity.notFound().build();
        if ("dado de alta".equalsIgnoreCase(hosp.getEstado()))
            return ResponseEntity.badRequest().body("⚠️ El paciente ya fue dado de alta");

        hosp.setEstado("dado de alta");
        hosp.setFechaAlta(LocalDate.now());
        hospRepo.save(hosp);

        Habitacion hab = hosp.getHabitacion();
        if (hab != null) {
            hab.setEstado("disponible");
            habRepo.save(hab);
        }

        return ResponseEntity.ok("✅ Paciente dado de alta correctamente");
    }

    // 🔹 5. Eliminar hospitalización
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Hospitalizacion hosp = hospRepo.findById(id).orElse(null);
        if (hosp == null) return ResponseEntity.notFound().build();

        Habitacion hab = hosp.getHabitacion();
        if (hab != null) {
            hab.setEstado("disponible");
            habRepo.save(hab);
        }

        hospRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
