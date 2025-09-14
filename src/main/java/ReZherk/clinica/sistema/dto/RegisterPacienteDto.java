package ReZherk.clinica.sistema.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterPacienteDto extends UsuarioBaseDto {
 @Valid
 @NotNull(message = "Los detalles del paciente son obligatorios")
 private PacienteDetalleDto pacienteDetalle;
}
