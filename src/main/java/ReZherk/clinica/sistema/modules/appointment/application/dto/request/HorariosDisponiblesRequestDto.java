package ReZherk.clinica.sistema.modules.appointment.application.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorariosDisponiblesRequestDto {

 @NotNull(message = "El ID del médico es obligatorio")
 @Positive(message = "El ID del médico debe ser positivo")
 private Integer idMedico;

 @NotNull(message = "La fecha es obligatoria")
 @FutureOrPresent(message = "La fecha debe ser hoy o futura")
 private LocalDate fecha;
}
