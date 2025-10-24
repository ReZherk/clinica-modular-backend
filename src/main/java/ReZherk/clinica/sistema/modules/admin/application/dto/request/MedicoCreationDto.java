package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class MedicoCreationDto extends UsuarioBaseDto {

 @Valid
 @NotNull(message = "Los detalles del medico son obligatorios")
 private MedicoDetalleDto medicoDetalle;

}
