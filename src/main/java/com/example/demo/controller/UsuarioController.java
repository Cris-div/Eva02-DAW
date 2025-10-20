package com.example.demo.controller;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 📋 Listar usuarios
    @GetMapping
    public List<Usuario> listar() {
        return repo.findAll();
    }

    // 🧾 Registrar nuevo usuario
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        try {
            if (repo.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
                return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
            }

            usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
            Usuario nuevo = repo.save(usuario);
            nuevo.setContraseña(null); // no devolver la contraseña
            return ResponseEntity.created(URI.create("/api/usuarios/" + nuevo.getIdUsuario())).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al registrar usuario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario datos) {
        Optional<Usuario> userOpt = repo.findByNombreUsuario(datos.getNombreUsuario());
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            if (passwordEncoder.matches(datos.getContraseña(), user.getContraseña())) {
                // ✅ Devuelve el usuario como JSON (sin contraseña)
                user.setContraseña(null);
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(401).body("Credenciales inválidas");
    }
}
