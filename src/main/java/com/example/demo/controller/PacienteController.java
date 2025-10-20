package com.example.demo.controller;

import com.example.demo.entity.Paciente;
import com.example.demo.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*") // ✅ Permite peticiones desde el frontend (React, etc.)
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    // 🔹 Listar todos los pacientes
    @GetMapping
    public ResponseEntity<List<Paciente>> listar() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        return ResponseEntity.ok(pacientes);
    }

    // 🔹 Listar pacientes activos y no hospitalizados
    @GetMapping("/disponibles")
    public ResponseEntity<List<Paciente>> listarNoHospitalizados() {
        List<Paciente> disponibles = pacienteRepository.findPacientesNoHospitalizados();
        return ResponseEntity.ok(disponibles);
    }

    // 🔹 Obtener un paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtener(@PathVariable Long id) {
        return pacienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Crear nuevo paciente
    @PostMapping
    public ResponseEntity<Paciente> crear(@RequestBody Paciente paciente) {
        if (paciente.getDni() == null || paciente.getDni().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Paciente nuevo = pacienteRepository.save(paciente);
        return ResponseEntity.created(URI.create("/api/pacientes/" + nuevo.getIdPaciente())).body(nuevo);
    }

    // 🔹 Actualizar paciente existente
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizar(@PathVariable Long id, @RequestBody Paciente paciente) {
        return pacienteRepository.findById(id)
                .map(existente -> {
                    existente.setNombres(paciente.getNombres());
                    existente.setApellidos(paciente.getApellidos());
                    existente.setDni(paciente.getDni());
                    existente.setDireccion(paciente.getDireccion());
                    existente.setTelefono(paciente.getTelefono());
                    existente.setCorreo(paciente.getCorreo());
                    existente.setSexo(paciente.getSexo());
                    existente.setEstado(paciente.getEstado());
                    existente.setFechaNacimiento(paciente.getFechaNacimiento());
                    return ResponseEntity.ok(pacienteRepository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Eliminar paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!pacienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pacienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
