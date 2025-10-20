package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/detalle-receta")
public class DetalleRecetaController {

    @Autowired
    private DetalleRecetaRepository detalleRepo;
    @Autowired
    private RecetaMedicaRepository recetaRepo;

    @GetMapping
    public List<DetalleReceta> listar() {
        return detalleRepo.findAll();
    }

    @PostMapping("/{idReceta}")
    public ResponseEntity<DetalleReceta> crear(@PathVariable Long idReceta, @RequestBody DetalleReceta detalle) {
        RecetaMedica receta = recetaRepo.findById(idReceta).orElse(null);
        if (receta == null) return ResponseEntity.notFound().build();

        detalle.setRecetaMedica(receta);
        DetalleReceta nuevo = detalleRepo.save(detalle);
        return ResponseEntity.created(URI.create("/api/detalle-receta/" + nuevo.getIdDetalleReceta())).body(nuevo);
    }
}
