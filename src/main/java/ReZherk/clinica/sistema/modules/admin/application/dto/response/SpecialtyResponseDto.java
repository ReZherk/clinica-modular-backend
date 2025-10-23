package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import java.math.BigDecimal;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SpecialtyResponseDto {
 private Integer id;
 private String nombreEspecialidad;
 private String descripcion;
 private BigDecimal costo;
 private Boolean activo;
 private Byte duracion;
}
