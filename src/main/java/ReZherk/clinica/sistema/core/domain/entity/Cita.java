package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ReZherk.clinica.sistema.core.shared.enums.EstadoCita;

@Entity
@Table(name = "cita")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_cita")
 private Integer idCita;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_medico_horario", nullable = false)
 private MedicoHorario medicoHorario;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_usuario", nullable = false)
 private Usuario paciente;

 @Column(name = "fecha", nullable = false)
 private LocalDate fecha;

 @Column(name = "hora", nullable = false)
 private LocalTime hora;

 @Enumerated(EnumType.STRING)
 @Builder.Default
 @Column(name = "estado", nullable = false)
 private EstadoCita estado = EstadoCita.RESERVADA;

 @Column(name = "motivo", length = 500)
 private String motivo;

 @Column(name = "enlace_reunion", length = 500)
 private String enlaceReunion;

 @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
 private DetalleCita detalleCita;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_pago")
 private Pago pago;

 @Column(name = "fecha_creacion", nullable = false, updatable = false)
 private LocalDateTime fechaCreacion;

 @Column(name = "fecha_actualizacion")
 private LocalDateTime fechaActualizacion;

 @PrePersist
 protected void onCreate() {
  fechaCreacion = LocalDateTime.now();
 }

 @PreUpdate
 protected void onUpdate() {
  fechaActualizacion = LocalDateTime.now();
 }

}
