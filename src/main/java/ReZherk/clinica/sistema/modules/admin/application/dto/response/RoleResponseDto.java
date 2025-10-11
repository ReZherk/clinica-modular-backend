package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import java.util.Set;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDto {

 private Integer id;
 private String nombre;
 private String descripcion;
 private Boolean estadoRegistro;
 private Set<PermissionDto> permisos;
 private CountResponse cantidad;

}
