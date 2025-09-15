package ReZherk.clinica.sistema.core.domain.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PerfilOpcionMenuId implements Serializable {
 private Integer idPerfil;
 private Integer idOpcionMenu;
}