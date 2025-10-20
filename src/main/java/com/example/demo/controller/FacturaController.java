package com.example.demo.controller;

import com.example.demo.entity.Factura;
import com.example.demo.entity.Paciente;
import com.example.demo.repository.FacturaRepository;
import com.example.demo.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "*")
public class FacturaController {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public List<Factura> listarFacturas() {
        return facturaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Factura> crearFactura(@RequestBody Factura factura) {
        // Verifica que haya un paciente válido
        if (factura.getPaciente() == null || factura.getPaciente().getIdPaciente() == 0) {
            return ResponseEntity.badRequest().build();
        }

        Paciente paciente = pacienteRepository.findById(factura.getPaciente().getIdPaciente())
                .orElse(null);

        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }

        factura.setPaciente(paciente);
        Factura nuevaFactura = facturaRepository.save(factura);
        return ResponseEntity.ok(nuevaFactura);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerFacturaPorId(@PathVariable Long id) {
        return facturaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFactura(@PathVariable Long id) {
        if (!facturaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        facturaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
