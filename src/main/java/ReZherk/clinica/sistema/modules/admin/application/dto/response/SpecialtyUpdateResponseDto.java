package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialtyUpdateResponseDto {
 private Integer id;
 private String nombreEspecialidad;
 private String descripcion;
 private BigDecimal tarifa;
 private Boolean estadoRegistro;
}
