package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequestDto {

 @NotBlank(message = "El nombre del rol es obligatorio")
 private String nombre;

 private String descripcion;

 @NotNull(message = "La lista de permisos no puede ser nula")
 @Size(min = 1, message = "Debe incluir al menos un permiso")
 private Set<String> permissionActionKeys;
}
