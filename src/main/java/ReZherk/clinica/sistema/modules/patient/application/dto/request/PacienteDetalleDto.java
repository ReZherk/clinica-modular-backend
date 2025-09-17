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

 @Past(message = "La fecha de nacimiento debe ser en el pasado")
 @JsonFormat(pattern = "yyyy-MM-dd")
 private LocalDate fechaNacimiento;

 @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
 private String direccion;

 @Size(max = 100)
 private String departamento;

 @Size(max = 100)
 private String provincia;

 @Size(max = 100)
 private String distrito;
}
