package ReZherk.clinica.sistema.modules.payment.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoSeguroRequestDto {

 @NotNull(message = "El ID de la cita es obligatorio")
 @Positive(message = "El ID de la cita debe ser positivo")
 private Integer idCita;

 @NotNull(message = "El ID del paciente es obligatorio")
 @Positive(message = "El ID del paciente debe ser positivo")
 private Integer idPaciente;
}
