package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Perfil_OpcionMenu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PerfilOpcionMenuId.class)
public class PerfilOpcionMenu {
 @Id
 @Column(name = "Id_Perfil")
 private Integer idPerfil;

 @Id
 @Column(name = "Id_OpcionMenu")
 private Integer idOpcionMenu;
}
