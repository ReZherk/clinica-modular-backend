package ReZherk.clinica.sistema.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

 @NotBlank(message = "El nombre es obligatorio")
 @Size(max = 50, message = "El nombre no debe exceder 50 caracteres")
 private String nombres;

 @NotBlank(message = "El apellido es obligatorio")
 @Size(max = 100)
 private String apellidos;

 @NotBlank(message = "La contraseña es obligatoria")
 @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
 private String password;

 @NotBlank(message = "El email es obligatorio")
 @Email(message = "Debe ser un email válido")
 private String email;

 @Size(max = 20)
 private String telefono;
}
