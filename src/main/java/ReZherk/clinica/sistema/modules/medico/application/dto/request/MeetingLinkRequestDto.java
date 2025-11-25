package ReZherk.clinica.sistema.modules.medico.application.dto.request;

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
public class MeetingLinkRequestDto {

 @NotNull(message = "El ID del medico es obligatorio")
 @Positive(message = "El ID del medico debe ser positivo")
 private Integer idMedico;

 @NotNull(message = "La fecha es obligatoria")
 private LocalDate fecha;

 @NotBlank(message = "El enlace de reunion es obligatorio")
 @Pattern(regexp = "^https?://.*", message = "El enlace debe ser una URL valida")
 @Size(max = 500, message = "El enlace no puede exceder 500 caracteres")
 private String enlaceReunion;
}
