package ReZherk.clinica.sistema.core.domain.entity;

import ReZherk.clinica.sistema.core.shared.enums.EstadoMedicoHorario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Entity
@Table(name = "medico_horario", uniqueConstraints = {
  @UniqueConstraint(columnNames = { "id_usuario", "id_horario" })
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoHorario {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_medico_horario")
 private Integer idMedicoHorario;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_usuario", nullable = false)
 private Usuario medico;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_horario", nullable = false)
 private Horario horario;

 @Builder.Default
 @Enumerated(EnumType.STRING)
 @Column(name = "estado", length = 10, nullable = false)
 private EstadoMedicoHorario estado = EstadoMedicoHorario.ACTIVO;

 @OneToMany(mappedBy = "medicoHorario", cascade = CascadeType.ALL, orphanRemoval = true)
 private List<Cita> citas;
}
