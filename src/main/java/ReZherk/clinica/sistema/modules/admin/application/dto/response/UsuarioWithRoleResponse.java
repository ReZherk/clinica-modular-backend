package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioWithRoleResponse {
 private UsuarioBaseDto usuario;
 private String rolActual;
}