package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignarHorariosRequestDto {

 @NotNull(message = "El ID del médico es obligatorio")
 private Integer idMedico;

 @NotEmpty(message = "Debe proporcionar al menos un horario")
 @Valid
 private List<Integer> horarios;

 @NotNull(message = "Las horas semanales son obligatorias")
 @Min(value = 24, message = "Las horas semanales mínimas son 24")
 @Max(value = 48, message = "Las horas semanales máximas son 48")
 private Integer horasSemanales;
}
