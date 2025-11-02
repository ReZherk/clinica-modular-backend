package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import jakarta.validation.Valid;
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
 private List<HorarioRequestDto> horarios;
}
