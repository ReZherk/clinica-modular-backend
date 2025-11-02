package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.*;

import ReZherk.clinica.sistema.core.shared.enums.DiaSemana;

@Entity
@Table(name = "horario")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Horario {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_horario")
 private Integer idHorario;

 @Enumerated(EnumType.STRING)
 @Column(name = "dia_semana", nullable = false, length = 15)
 private DiaSemana diaSemana;

 @Column(name = "hora_inicio", nullable = false)
 private LocalTime horaInicio;

 @Column(name = "hora_fin", nullable = false)
 private LocalTime horaFin;

 @OneToMany(mappedBy = "horario", cascade = CascadeType.ALL, orphanRemoval = true)
 private List<MedicoHorario> medicoHorarios;
}
