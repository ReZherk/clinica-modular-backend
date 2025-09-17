package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "paciente_detalle")
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

 @Column(name = "FechaNacimiento")
 private java.time.LocalDate fechaNacimiento;

 @Column(name = "Direccion")
 private String direccion;

 @Column(name = "Departamento", length = 100)
 private String departamento;

 @Column(name = "Provincia", length = 100)
 private String provincia;

 @Column(name = "Distrito", length = 100)
 private String distrito;
}
