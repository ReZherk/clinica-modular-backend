package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoDetalleDto {

 @Pattern(regexp = "\\d{5,6}", message = "El CMP debe tener 5 o 6 dígitos numéricos")
 private String cmp;

 @NotNull(message = "La especialidad es obligatoria")
 private Integer idEspecialidad;

 @NotNull(message = "El número de horas semanales es obligatorio")
 @Min(value = 24, message = "La jornada mínima permitida es de 20 horas semanales")
 @Max(value = 48, message = "La jornada máxima permitida es de 48 horas semanales")
 private Integer horasSemanales;
}
