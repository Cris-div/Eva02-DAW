package com.example.demo.controller;
import com.example.demo.entity.AntecedenteMedico;
import com.example.demo.entity.HistoriaClinica;
import com.example.demo.repository.AntecedenteMedicoRepository;
import com.example.demo.repository.HistoriaClinicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/antecedentes")
public class AntecedenteMedicoController {

    @Autowired
    private AntecedenteMedicoRepository antecedenteRepo;

    @Autowired
    private HistoriaClinicaRepository historiaRepo;

    @GetMapping
    public List<AntecedenteMedico> listar() {
        return antecedenteRepo.findAll();
    }

    @PostMapping("/{idHistoria}")
    public ResponseEntity<AntecedenteMedico> registrarAntecedente(@PathVariable Long idHistoria, @RequestBody AntecedenteMedico antecedente) {
        HistoriaClinica historia = historiaRepo.findById(idHistoria).orElse(null);
        if (historia == null) {
            return ResponseEntity.notFound().build();
        }

        antecedente.setHistoriaClinica(historia);
        AntecedenteMedico nuevo = antecedenteRepo.save(antecedente);
        return ResponseEntity.created(URI.create("/api/antecedentes/" + nuevo.getIdAntecedente())).body(nuevo);
    }
}
