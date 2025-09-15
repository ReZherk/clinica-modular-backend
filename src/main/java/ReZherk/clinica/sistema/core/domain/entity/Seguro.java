package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Seguro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seguro {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Seguro")
 private Integer id;

 @Column(name = "NombreSeguro", nullable = false, length = 100)
 private String nombre;
}
