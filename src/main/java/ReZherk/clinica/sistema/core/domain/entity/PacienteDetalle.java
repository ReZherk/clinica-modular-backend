package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Paciente_Detalle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteDetalle {

 @Id
 @Column(name = "Id_Usuario")
 private Integer idUsuario;

 @OneToOne
 @MapsId
 @JoinColumn(name = "Id_Usuario")
 private Usuario usuario;

 @Column(name = "DNI", length = 8, unique = true, columnDefinition = "CHAR(8)")
 private String dni;

 @Column(name = "FechaNacimiento")
 private java.time.LocalDate fechaNacimiento;

 @Column(name = "Direccion")
 private String direccion;
}
