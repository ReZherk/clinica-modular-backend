package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Ubigeo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ubigeo {
 @Id
 @Column(name = "Id_Ubigeo")
 private Integer id;

 @Column(name = "Departamento", length = 100)
 private String departamento;

 @Column(name = "Provincia", length = 100)
 private String provincia;

 @Column(name = "Distrito", length = 100)
 private String distrito;
}
