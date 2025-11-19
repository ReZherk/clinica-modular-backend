package ReZherk.clinica.sistema.modules.payment.application.dto.request;

import ReZherk.clinica.sistema.core.shared.enums.TipoTarjeta;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoTarjetaRequestDto {

 @NotNull(message = "El ID de la cita es obligatorio")
 @Positive(message = "El ID de la cita debe ser positivo")
 private Integer idCita;

 @NotNull(message = "El tipo de tarjeta es obligatorio")
 private TipoTarjeta tipoTarjeta;

 @NotBlank(message = "El numero de tarjeta es obligatorio")
 @Pattern(regexp = "^[0-9]{16}$", message = "El numero de tarjeta debe tener 16 digitos")
 private String numeroTarjeta;

 @NotBlank(message = "El titular de la tarjeta es obligatorio")
 @Size(min = 3, max = 100, message = "El titular debe tener entre 3 y 100 caracteres")
 private String titular;

 @NotBlank(message = "La fecha de expiracion es obligatoria")
 @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Formato debe ser MM/YY")
 private String fechaExpiracion;

 @NotBlank(message = "El CVV es obligatorio")
 @Pattern(regexp = "^[0-9]{3,4}$", message = "El CVV debe tener 3 o 4 digitos")
 private String cvv;
}
