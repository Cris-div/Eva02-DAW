package com.example.demo.controller;

import com.example.demo.entity.Cita;
import com.example.demo.entity.Consulta;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Paciente;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.ConsultaRepository;
import com.example.demo.repository.MedicoRepository;
import com.example.demo.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaRepository consultaRepo;
    @Autowired
    private PacienteRepository pacienteRepo;
    @Autowired
    private MedicoRepository medicoRepo;
    @Autowired
    private CitaRepository citaRepo;

    @GetMapping
    public List<Consulta> listar() {
        return consultaRepo.findAll();
    }

    @PostMapping("/{idPaciente}/{idMedico}/{idCita}")
    public ResponseEntity<Consulta> crearConsulta(
            @PathVariable Long idPaciente,
            @PathVariable Long idMedico,
            @PathVariable Long idCita,
            @RequestBody Consulta consulta) {

        Paciente p = pacienteRepo.findById(idPaciente).orElse(null);
        Medico m = medicoRepo.findById(idMedico).orElse(null);
        Cita c = citaRepo.findById(idCita).orElse(null);

        if (p == null || m == null || c == null) return ResponseEntity.badRequest().build();

        consulta.setPaciente(p);
        consulta.setMedico(m);
        consulta.setCita(c);

        Consulta nueva = consultaRepo.save(consulta);
        return ResponseEntity.created(URI.create("/api/consultas/" + nueva.getIdConsulta())).body(nueva);
    }
}
