package com.example.demo.controller;

import com.example.demo.entity.Habitacion;
import com.example.demo.repository.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
@CrossOrigin(origins = "*")
public class HabitacionController {

    @Autowired
    private HabitacionRepository repo;

    // 🔹 Listar todas las habitaciones
    @GetMapping
    public ResponseEntity<List<Habitacion>> listar() {
        List<Habitacion> habitaciones = repo.findAll();
        return ResponseEntity.ok(habitaciones);
    }

    // 🔹 Listar solo las habitaciones disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<Habitacion>> listarDisponibles() {
        List<Habitacion> disponibles = repo.findByEstado("disponible");
        return ResponseEntity.ok(disponibles);
    }

    // 🔹 Obtener una habitación por ID
    @GetMapping("/{id}")
    public ResponseEntity<Habitacion> obtenerPorId(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Crear nueva habitación
    @PostMapping
    public ResponseEntity<Habitacion> crear(@RequestBody Habitacion habitacion) {
        if (habitacion.getNumero() == null || habitacion.getNumero().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Habitacion nueva = repo.save(habitacion);
        return ResponseEntity.created(URI.create("/api/habitaciones/" + nueva.getIdHabitacion())).body(nueva);
    }

    // 🔹 Cambiar estado de habitación (ocupada, disponible, mantenimiento)
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return repo.findById(id)
                .map(h -> {
                    h.setEstado(estado);
                    repo.save(h);
                    return ResponseEntity.ok("✅ Estado actualizado a: " + estado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Eliminar habitación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
