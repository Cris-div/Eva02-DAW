package com.example.demo.controller;

import com.example.demo.entity.Especialidad;
import com.example.demo.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadRepository repo;

    @GetMapping
    public List<Especialidad> listar() { return repo.findAll(); }

    @PostMapping
    public ResponseEntity<Especialidad> crear(@RequestBody Especialidad esp) {
        Especialidad nueva = repo.save(esp);
        return ResponseEntity.created(URI.create("/api/especialidades/" + nueva.getIdEspecialidad())).body(nueva);
    }
}
