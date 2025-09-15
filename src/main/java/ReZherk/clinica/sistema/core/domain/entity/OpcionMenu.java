package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "OpcionMenu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpcionMenu {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_OpcionMenu")
 private Integer id;

 @Column(name = "Nombre", length = 100)
 private String nombre;

 @Column(name = "UrlMenu", length = 100)
 private String urlMenu;

 @Column(name = "Descripcion", length = 255)
 private String descripcion;

 @ManyToOne
 @JoinColumn(name = "Id_Padre")
 private OpcionMenu padre;

 @Column(name = "EstadoRegistro")
 private Boolean estadoRegistro;
}
