package ReZherk.clinica.sistema.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaRequestDTO {

 @NotNull(message = "La fecha y hora son obligatorias")
 @Future(message = "La cita debe ser en una fecha futura")
 private LocalDateTime fechaHora;

 @NotNull(message = "El paciente es obligatorio")
 private Integer idPaciente;

 @NotNull(message = "El médico es obligatorio")
 private Integer idMedico;

 @NotNull(message = "La especialidad es obligatoria")
 private Integer idEspecialidad;

 @NotNull(message = "La sede es obligatoria")
 private Integer idSede;

 @NotNull(message = "La tarifa es obligatoria")
 private Integer idTarifa;
}
