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
public class CitaReprogramRequestDto {

 @NotNull(message = "El ID de la cita es obligatorio")
 @Positive(message = "El ID de la cita debe ser positivo")
 private Integer idCita;

 @NotNull(message = "La nueva fecha es obligatoria")
 @Future(message = "La nueva fecha debe ser futura")
 private LocalDate nuevaFecha;

 @NotNull(message = "La nueva hora es obligatoria")
 private LocalTime nuevaHora;

 private Integer nuevoIdMedicoHorario;
}