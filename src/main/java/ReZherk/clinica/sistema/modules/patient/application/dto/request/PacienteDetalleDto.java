package ReZherk.clinica.sistema.modules.patient.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteDetalleDto {
 @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
 private String dni;

 @Past(message = "La fecha de nacimiento debe ser en el pasado")
 @JsonFormat(pattern = "yyyy-MM-dd")
 private LocalDate fechaNacimiento;

 @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
 private String direccion;
}
