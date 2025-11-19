package ReZherk.clinica.sistema.modules.payment.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VincularSeguroRequestDto {

 @NotNull(message = "El ID del paciente es obligatorio")
 @Positive(message = "El ID del paciente debe ser positivo")
 private Integer idPaciente;

 @NotNull(message = "El ID del seguro es obligatorio")
 @Positive(message = "El ID del seguro debe ser positivo")
 private Integer idSeguro;

 @NotBlank(message = "El numero de poliza es obligatorio")
 @Size(min = 5, max = 50, message = "El numero de poliza debe tener entre 5 y 50 caracteres")
 private String numeroPoliza;

 @NotNull(message = "La fecha de inicio de vigencia es obligatoria")
 private LocalDate fechaVigenciaInicio;

 @NotNull(message = "La fecha de fin de vigencia es obligatoria")
 @Future(message = "La fecha de fin debe ser futura")
 private LocalDate fechaVigenciaFin;
}
