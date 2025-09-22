package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "perfil_opcionmenu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilOpcionMenu {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_RolOpcionMenu")
 private Integer id;

 @ManyToOne
 @JoinColumn(name = "Id_Perfil")
 private RolPerfil rol;

 @ManyToOne
 @JoinColumn(name = "Id_OpcionMenu")
 private OpcionMenu opcionMenu;
}
