package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seguro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seguro {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_seguro")
 private Integer idSeguro;

 @Column(name = "nombre_seguro", nullable = false, length = 100)
 private String nombreSeguro;

 @Column(name = "descripcion", length = 255)
 private String descripcion;

 @Column(name = "cubre_costo_total", nullable = false)
 @Builder.Default
 private Boolean cubreCostoTotal = true;

 @Column(name = "estado_registro", nullable = false)
 @Builder.Default
 private Boolean estadoRegistro = true;

 @Column(name = "fecha_creacion", nullable = false, updatable = false, insertable = false)
 private LocalDateTime fechaCreacion;

}
