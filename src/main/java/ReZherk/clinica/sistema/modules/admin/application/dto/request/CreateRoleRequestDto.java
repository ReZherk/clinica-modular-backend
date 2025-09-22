package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoleRequestDto {

 @NotBlank(message = "El nombre del rol es obligatorio")
 private String nombre;

 private String descripcion;

 /** Lista opcional de IDs de opciones de menú (acciones) que se asignarán */
 private List<Integer> accionesIds;
}
