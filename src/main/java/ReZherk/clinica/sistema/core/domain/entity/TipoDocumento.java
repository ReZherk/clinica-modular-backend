package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_documento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoDocumento {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_TipoDocumento")
 private Integer id;

 @Column(name = "Nombre", nullable = false, length = 50)
 private String nombre;

 @Column(name = "Descripcion", length = 255)
 private String descripcion;

 @Column(name = "EstadoRegistro", nullable = false)
 @Builder.Default
 private Boolean estadoRegistro = true;
}
