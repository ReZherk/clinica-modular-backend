package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialtyUpdateDto {
    private String nombreEspecialidad;
    private String descripcion;
    private BigDecimal tarifa;
    private Byte duracion;
    private Boolean estadoRegistro;
}