package ReZherk.clinica.sistema.dto;

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
