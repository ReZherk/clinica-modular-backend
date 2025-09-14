package ReZherk.clinica.sistema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRolPerfilDto {
 @NotBlank(message = "El nombre del rol es obligatorio")
 @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
 private String nombre;

 @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
 private String descripcion;
}
// post