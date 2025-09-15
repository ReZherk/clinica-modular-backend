package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Sede")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sede {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Sede")
 private Integer id;

 @Column(name = "Nombre", length = 100)
 private String nombre;

 @ManyToOne
 @JoinColumn(name = "Id_Ubigeo")
 private Ubigeo ubigeo;

 @Column(name = "Direccion", length = 255)
 private String direccion;

 @Column(name = "Telefono", length = 20)
 private String telefono;
}
