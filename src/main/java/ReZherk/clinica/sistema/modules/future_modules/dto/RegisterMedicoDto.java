package ReZherk.clinica.sistema.modules.future_modules.dto;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterMedicoDto extends UsuarioBaseDto {
 @Valid
 @NotNull(message = "Los detalles del médico son obligatorios")
 private MedicoDetalleDto medicoDetalle;
}
