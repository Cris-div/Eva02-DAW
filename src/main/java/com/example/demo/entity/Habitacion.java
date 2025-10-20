package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "habitacion")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHabitacion;

    private String numero;
    private String tipo;
    private String estado = "disponible";

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("habitacion") // evita ciclo
    private List<Hospitalizacion> hospitalizaciones;

    // Getters y Setters
    public Long getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Long idHabitacion) { this.idHabitacion = idHabitacion; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<Hospitalizacion> getHospitalizaciones() { return hospitalizaciones; }
    public void setHospitalizaciones(List<Hospitalizacion> hospitalizaciones) { this.hospitalizaciones = hospitalizaciones; }
}
