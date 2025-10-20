package com.example.demo.controller;

import com.example.demo.entity.HistoriaClinica;
import com.example.demo.entity.Paciente;
import com.example.demo.repository.HistoriaClinicaRepository;
import com.example.demo.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/historias")
public class HistoriaClinicaController {

    @Autowired
    private HistoriaClinicaRepository historiaRepo;

    @Autowired
    private PacienteRepository pacienteRepo;

    @GetMapping
    public List<HistoriaClinica> listar() {
        return historiaRepo.findAll();
    }

    @PostMapping("/{idPaciente}")
    public ResponseEntity<HistoriaClinica> crearHistoria(@PathVariable Long idPaciente, @RequestBody HistoriaClinica historia) {
        Paciente paciente = pacienteRepo.findById(idPaciente).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }

        historia.setPaciente(paciente);
        HistoriaClinica nueva = historiaRepo.save(historia);
        return ResponseEntity.created(URI.create("/api/historias/" + nueva.getIdHistoria())).body(nueva);
    }
}
