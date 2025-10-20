package com.example.demo.controller;
import com.example.demo.entity.Cita;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Paciente;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaRepository citaRepo;

    @Autowired
    private PacienteRepository pacienteRepo;

    @Autowired
    private MedicoRepository medicoRepo;

    @GetMapping
    public List<Cita> listar() {
        return citaRepo.findAll();
    }

    @PostMapping("/{idPaciente}/{idMedico}")
    public ResponseEntity<Cita> crearCita(@PathVariable Long idPaciente, @PathVariable Long idMedico, @RequestBody Cita cita) {
        Paciente paciente = pacienteRepo.findById(idPaciente).orElse(null);
        Medico medico = medicoRepo.findById(idMedico).orElse(null);

        if (paciente == null || medico == null)
            return ResponseEntity.badRequest().build();

        cita.setPaciente(paciente);
        cita.setMedico(medico);
        Cita nueva = citaRepo.save(cita);

        return ResponseEntity.created(URI.create("/api/citas/" + nueva.getIdCita())).body(nueva);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Cita> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return citaRepo.findById(id)
                .map(cita -> {
                    cita.setEstado(estado);
                    return ResponseEntity.ok(citaRepo.save(cita));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
