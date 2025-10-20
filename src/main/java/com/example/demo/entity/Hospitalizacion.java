package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hospitalizacion")
public class Hospitalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHosp;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    @JsonIgnoreProperties("hospitalizaciones") // evita recursión
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    @JsonIgnoreProperties("hospitalizaciones") // evita recursión
    private Habitacion habitacion;

    private LocalDate fechaIngreso = LocalDate.now();
    private LocalDate fechaAlta;
    private String diagnosticoIngreso;
    private String estado = "activo";

    // Getters y Setters
    public Long getIdHosp() { return idHosp; }
    public void setIdHosp(Long idHosp) { this.idHosp = idHosp; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    public String getDiagnosticoIngreso() { return diagnosticoIngreso; }
    public void setDiagnosticoIngreso(String diagnosticoIngreso) { this.diagnosticoIngreso = diagnosticoIngreso; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
