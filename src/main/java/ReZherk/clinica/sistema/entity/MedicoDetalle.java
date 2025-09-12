package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Medico_Detalle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoDetalle {

 @Id
 @Column(name = "Id_Usuario")
 private Integer idUsuario;

 @OneToOne
 @MapsId
 @JoinColumn(name = "Id_Usuario")
 private Usuario usuario;

 @Column(name = "CMP", length = 20)
 private String cmp;

 @ManyToOne
 @JoinColumn(name = "Id_Especialidad")
 private Especialidad especialidad;
}
