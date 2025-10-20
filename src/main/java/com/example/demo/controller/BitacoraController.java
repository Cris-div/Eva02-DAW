package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bitacora")
public class BitacoraController {

    @Autowired
    private BitacoraRepository bitacoraRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping
    public List<Bitacora> listar() {
        return bitacoraRepo.findAll();
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Bitacora> registrarAccion(@PathVariable Long idUsuario, @RequestBody Bitacora bitacora) {
        Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);
        if (usuario == null) return ResponseEntity.notFound().build();

        bitacora.setUsuario(usuario);
        Bitacora nueva = bitacoraRepo.save(bitacora);
        return ResponseEntity.created(URI.create("/api/bitacora/" + nueva.getIdBitacora())).body(nueva);
    }
}