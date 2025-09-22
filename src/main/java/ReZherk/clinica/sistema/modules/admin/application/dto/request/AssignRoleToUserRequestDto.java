package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import lombok.*;
import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignRoleToUserRequestDto extends UsuarioBaseDto {

 @NotNull(message = "Debe indicar el ID del rol")
 private Integer idRol;
}
