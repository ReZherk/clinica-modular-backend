package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Cita")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Cita")
 private Integer id;

 @Column(name = "FechaHora", nullable = false)
 private LocalDateTime fechaHora;

 @ManyToOne
 @JoinColumn(name = "Id_UsuarioPaciente")
 private PacienteDetalle paciente;

 @ManyToOne
 @JoinColumn(name = "Id_UsuarioMedico")
 private MedicoDetalle medico;

 @ManyToOne
 @JoinColumn(name = "Id_Especialidad")
 private Especialidad especialidad;

 @ManyToOne
 @JoinColumn(name = "Id_Sede")
 private Sede sede;

 @ManyToOne
 @JoinColumn(name = "Id_Tarifa")
 private Tarifario tarifa;

 @Builder.Default
 @Column(name = "Estado", nullable = false)
 private Boolean estado = true;
}
