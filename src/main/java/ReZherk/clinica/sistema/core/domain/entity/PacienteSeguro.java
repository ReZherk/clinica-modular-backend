package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paciente_seguro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteSeguro {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_paciente_seguro")
 private Integer idPacienteSeguro;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_usuario", nullable = false)
 private Usuario paciente;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_seguro", nullable = false)
 private Seguro seguro;

 @Column(name = "numero_poliza", length = 50)
 private String numeroPoliza;

 @Column(name = "fecha_vigencia_inicio")
 private LocalDate fechaVigenciaInicio;

 @Column(name = "fecha_vigencia_fin")
 private LocalDate fechaVigenciaFin;

 @Column(name = "estado_activo", nullable = false)
 @Builder.Default
 private Boolean estadoActivo = true;

 @Column(name = "fecha_registro", nullable = false, updatable = false, insertable = false)
 private LocalDateTime fechaRegistro;

 /**
  * Verifica si la poliza esta vigente en la fecha actual
  */
 public boolean isVigente() {
  LocalDate hoy = LocalDate.now();
  return estadoActivo &&
    (fechaVigenciaInicio == null || !hoy.isBefore(fechaVigenciaInicio)) &&
    (fechaVigenciaFin == null || !hoy.isAfter(fechaVigenciaFin));
 }

}
