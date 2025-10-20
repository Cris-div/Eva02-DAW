package com.example.demo.controller;

import com.example.demo.entity.Consulta;
import com.example.demo.entity.RecetaMedica;
import com.example.demo.repository.ConsultaRepository;
import com.example.demo.repository.RecetaMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/recetas")
public class RecetaMedicaController {

    @Autowired
    private RecetaMedicaRepository recetaRepo;
    @Autowired
    private ConsultaRepository consultaRepo;

    @GetMapping
    public List<RecetaMedica> listar() {
        return recetaRepo.findAll();
    }

    @PostMapping("/{idConsulta}")
    public ResponseEntity<RecetaMedica> crear(@PathVariable Long idConsulta, @RequestBody RecetaMedica receta) {
        Consulta consulta = consultaRepo.findById(idConsulta).orElse(null);
        if (consulta == null) return ResponseEntity.notFound().build();

        receta.setConsulta(consulta);
        RecetaMedica nueva = recetaRepo.save(receta);
        return ResponseEntity.created(URI.create("/api/recetas/" + nueva.getIdReceta())).body(nueva);
    }
}
