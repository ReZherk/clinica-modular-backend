package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioRequestDto {

 @NotBlank(message = "El día de la semana es obligatorio")
 @Pattern(regexp = "^(Lunes|Martes|Miércoles|Jueves|Viernes|Sábado)$", message = "Día inválido. Debe ser: Lunes, Martes, Miércoles, Jueves, Viernes o Sábado")
 private String diaSemana;

 @NotBlank(message = "La hora de inicio es obligatoria")
 @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora inválido. Use HH:mm")
 private String horaInicio;

 @NotBlank(message = "La hora de fin es obligatoria")
 @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora inválido. Use HH:mm")
 private String horaFin;
}
