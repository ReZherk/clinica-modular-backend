package ReZherk.clinica.sistema.core.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class UsuarioBaseDto {

 @NotNull(message = "Debe seleccionar un tipo de documento")
 private Integer tipoDocumentoId; // FK a TipoDocumento

 @NotBlank(message = "El DNI es obligatorio")
 @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
 private String dni;

 @PastOrPresent(message = "La fecha de emisión no puede ser futura")
 @JsonFormat(pattern = "yyyy-MM-dd")
 private LocalDate fechaEmision;

 @NotBlank(message = "Los nombres son obligatorios")
 @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
 private String nombres;

 @NotBlank(message = "Los apellidos son obligatorios")
 @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
 private String apellidos;

 @NotBlank(message = "El email es obligatorio")
 @Email(message = "Formato de email inválido")
 @Size(max = 150, message = "El email no puede exceder 150 caracteres")
 private String email;

 @NotBlank(message = "La contraseña es obligatoria")
 @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
 private String password;

 @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
 private String telefono;
}
