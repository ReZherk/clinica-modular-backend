package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Usuario_Perfil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UsuarioPerfilId.class)
public class UsuarioPerfil {

 @Id
 @Column(name = "Id_Usuario")
 private Integer idUsuario;

 @Id
 @Column(name = "Id_Perfil")
 private Integer idPerfil;

 // Relaciones como propiedades separadas (no @Id)
 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "Id_Usuario", insertable = false, updatable = false)
 private Usuario usuario;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "Id_Perfil", insertable = false, updatable = false)
 private RolPerfil rolPerfil;
}
