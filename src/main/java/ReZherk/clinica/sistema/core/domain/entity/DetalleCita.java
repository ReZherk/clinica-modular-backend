package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "detalle_cita")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCita {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_detalle_cita")
 private Integer idDetalleCita;

 @OneToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_cita", nullable = false, unique = true)
 private Cita cita;

 @Column(name = "diagnostico", columnDefinition = "TEXT")
 private String diagnostico;

 @Column(name = "receta", columnDefinition = "TEXT")
 private String receta;

 @Column(name = "observaciones", columnDefinition = "TEXT")
 private String observaciones;

 @Column(name = "fecha_registro", nullable = false, updatable = false, insertable = false)
 private LocalDateTime fechaRegistro;

 @Column(name = "fecha_actualizacion")
 private LocalDateTime fechaActualizacion;

}
