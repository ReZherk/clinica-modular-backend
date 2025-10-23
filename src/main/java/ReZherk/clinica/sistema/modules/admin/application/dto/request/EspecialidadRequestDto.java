package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import lombok.*;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecialidadRequestDto {
 @NotBlank(message = "El nombre de la especialidad es obligatorio")
 @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
 private String nombreEspecialidad;

 @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres")
 private String descripcion;

 @NotNull(message = "La tarifa es obligatoria")
 @DecimalMin(value = "0.00", inclusive = false, message = "La tarifa debe ser mayor a 0")
 @Digits(integer = 8, fraction = 2, message = "La tarifa debe tener hasta 8 dígitos enteros y 2 decimales")
 private BigDecimal tarifa;

 @NotNull(message = "La duración es obligatoria")
 @Min(value = 30, message = "La duración mínima permitida es 30 minutos")
 @Max(value = 60, message = "La duración máxima permitida es 60 minutos")
 private Byte duracion;

}
