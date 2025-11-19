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
public class PagoYapeRequestDto {

 @NotNull(message = "El ID de la cita es obligatorio")
 @Positive(message = "El ID de la cita debe ser positivo")
 private Integer idCita;

 @NotBlank(message = "El numero de telefono es obligatorio")
 @Pattern(regexp = "^9[0-9]{8}$", message = "El numero debe tener 9 digitos y comenzar con 9")
 private String numeroTelefono;

 @NotBlank(message = "El codigo de operacion es obligatorio")
 @Size(min = 6, max = 20, message = "El codigo debe tener entre 6 y 20 caracteres")
 private String codigoOperacion;
}
