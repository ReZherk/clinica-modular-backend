package ReZherk.clinica.sistema.modules.medico.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCitaRequestDto {

 @NotNull(message = "El ID de la cita es obligatorio")
 @Positive(message = "El ID de la cita debe ser positivo")
 private Integer idCita;

 @NotBlank(message = "El diagnostico es obligatorio")
 @Size(min = 10, max = 5000, message = "El diagnostico debe tener entre 10 y 5000 caracteres")
 private String diagnostico;

 @Size(max = 5000, message = "La receta no puede exceder 5000 caracteres")
 private String receta;

 @Size(max = 5000, message = "Las observaciones no pueden exceder 5000 caracteres")
 private String observaciones;
}