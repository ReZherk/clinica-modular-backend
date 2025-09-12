package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "Horario_Medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioMedico {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Horario")
 private Integer id;

 @ManyToOne
 @JoinColumn(name = "Id_UsuarioMedico")
 private MedicoDetalle medico;

 @Column(name = "DiaSemana", length = 20)
 private String diaSemana;

 @Column(name = "HoraInicio")
 private LocalTime horaInicio;

 @Column(name = "HoraFin")
 private LocalTime horaFin;
}
