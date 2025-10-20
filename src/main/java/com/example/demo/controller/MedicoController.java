package com.example.demo.controller;

import com.example.demo.entity.Medico;
import com.example.demo.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repo;

    @GetMapping
    public List<Medico> listar() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<Medico> crear(@RequestBody Medico medico) {
        Medico nuevo = repo.save(medico);
        return ResponseEntity.created(URI.create("/api/medicos/" + nuevo.getIdMedico())).body(nuevo);
    }
}