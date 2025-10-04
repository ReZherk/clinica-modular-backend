package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountResponse {
 private long activos;
 private long inactivos;
}