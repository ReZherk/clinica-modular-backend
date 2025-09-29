package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionResponseDto {
 private Long id;
 private String name;
 private String description;
 private String actionKey;
}
