package ReZherk.clinica.sistema.core.domain.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UsuarioPerfilId implements Serializable {
 private Integer idUsuario;
 private Integer idPerfil; // corresponde a Id_Perfil en Roles_Perfil
}