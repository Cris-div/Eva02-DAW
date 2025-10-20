package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/detalle-factura")
public class DetalleFacturaController {

    @Autowired
    private DetalleFacturaRepository detalleRepo;

    @Autowired
    private FacturaRepository facturaRepo;

    @GetMapping
    public List<DetalleFactura> listar() {
        return detalleRepo.findAll();
    }

    // Agregar detalle a factura
    @PostMapping("/{idFactura}")
    public ResponseEntity<DetalleFactura> agregarDetalle(@PathVariable Long idFactura, @RequestBody DetalleFactura detalle) {
        Factura factura = facturaRepo.findById(idFactura).orElse(null);
        if (factura == null) return ResponseEntity.notFound().build();

        detalle.setFactura(factura);
        DetalleFactura nuevo = detalleRepo.save(detalle);

        // Actualizar total de factura
        factura.setTotal(factura.getTotal() + detalle.getMonto());
        facturaRepo.save(factura);

        return ResponseEntity.created(URI.create("/api/detalle-factura/" + nuevo.getIdDetalleFactura())).body(nuevo);
    }
}