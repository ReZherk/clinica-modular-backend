package ReZherk.clinica.sistema.modules.appointment.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaCreateRequestDto {

 @NotNull(message = "El ID del médico horario es obligatorio")
 @Positive(message = "El ID del médico horario debe ser positivo")
 private Integer idMedicoHorario;

 @NotNull(message = "El ID del paciente es obligatorio")
 @Positive(message = "El ID del paciente debe ser positivo")
 private Integer idPaciente;

 @NotNull(message = "La fecha de la cita es obligatoria")
 @Future(message = "La fecha de la cita debe ser futura")
 private LocalDate fecha;

 @NotNull(message = "La hora de la cita es obligatoria")
 private LocalTime hora;

 @Size(max = 500, message = "El motivo no puede exceder 500 caracteres")
 private String motivo;
}
