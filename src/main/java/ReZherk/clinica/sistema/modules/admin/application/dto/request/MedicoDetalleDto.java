package ReZherk.clinica.sistema.modules.admin.application.dto.request;

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
}
