package com.example.demo.controller;

import com.example.demo.entity.Consulta;
import com.example.demo.entity.Diagnostico;
import com.example.demo.repository.ConsultaRepository;
import com.example.demo.repository.DiagnosticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/diagnosticos")
public class DiagnosticoController {

    @Autowired
    private DiagnosticoRepository repo;
    @Autowired
    private ConsultaRepository consultaRepo;

    @GetMapping
    public List<Diagnostico> listar() {
        return repo.findAll();
    }

    @PostMapping("/{idConsulta}")
    public ResponseEntity<Diagnostico> crear(@PathVariable Long idConsulta, @RequestBody Diagnostico d) {
        Consulta consulta = consultaRepo.findById(idConsulta).orElse(null);
        if (consulta == null) return ResponseEntity.notFound().build();

        d.setConsulta(consulta);
        Diagnostico nuevo = repo.save(d);
        return ResponseEntity.created(URI.create("/api/diagnosticos/" + nuevo.getIdDiagnostico())).body(nuevo);
    }
}
