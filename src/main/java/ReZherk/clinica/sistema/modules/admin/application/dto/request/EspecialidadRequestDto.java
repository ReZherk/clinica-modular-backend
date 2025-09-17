package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecialidadRequestDto {
 private String nombreEspecialidad;
 private String descripcion;
 private BigDecimal tarifa;
}
