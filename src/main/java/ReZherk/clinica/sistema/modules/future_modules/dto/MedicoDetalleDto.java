package ReZherk.clinica.sistema.modules.future_modules.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoDetalleDto {
 @NotBlank(message = "El CMP es obligatorio")
 @Size(max = 20, message = "El CMP no puede exceder 20 caracteres")
 private String cmp;

 @NotNull(message = "La especialidad es obligatoria")
 private Integer idEspecialidad;
}
