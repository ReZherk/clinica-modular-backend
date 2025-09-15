package ReZherk.clinica.sistema.modules.future_modules.dto;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.PacienteDetalleDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterSuperAdminDto extends UsuarioBaseDto {
 @NotBlank(message = "El rol es obligatorio")
 private String rol; // PACIENTE, MEDICO, ADMIN_MEDICOS, ADMIN_HORARIOS

 @Valid
 private PacienteDetalleDto pacienteDetalle;

 @Valid
 private MedicoDetalleDto medicoDetalle;
}
// post