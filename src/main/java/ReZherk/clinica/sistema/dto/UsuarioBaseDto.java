package ReZherk.clinica.sistema.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class UsuarioBaseDto {
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
