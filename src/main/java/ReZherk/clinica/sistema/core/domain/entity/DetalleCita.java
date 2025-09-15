package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Detalle_Cita")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCita {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Detalle")
 private Integer id;

 @ManyToOne
 @JoinColumn(name = "Id_Cita")
 private Cita cita;

 @Column(name = "Diagnostico", length = 255)
 private String diagnostico;

 @Column(name = "Receta", length = 255)
 private String receta;
}
