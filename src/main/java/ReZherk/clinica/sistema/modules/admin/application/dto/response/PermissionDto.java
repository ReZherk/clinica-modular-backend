package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
 private Long id;
 private String name;
 private String description;
 private String actionKey;
}
