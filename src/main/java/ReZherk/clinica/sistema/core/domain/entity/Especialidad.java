package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "especialidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Especialidad {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Especialidad")
 private Integer id;

 @Column(name = "NombreEspecialidad", nullable = false, length = 100)
 private String nombreEspecialidad;

 @Column(name = "Descripcion")
 private String descripcion;

 @Column(name = "Tarifa", precision = 10, scale = 2)
 private BigDecimal tarifa;

 @Column(name = "EstadoRegistro", nullable = false)
 @Builder.Default
 private Boolean estadoRegistro = true;

 @Column(name = "Duracion", nullable = false)
 @Builder.Default
 private Byte Duracion = 30;
}
