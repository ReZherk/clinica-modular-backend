package ReZherk.clinica.sistema.modules.admin.application.dto.response;

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
}
