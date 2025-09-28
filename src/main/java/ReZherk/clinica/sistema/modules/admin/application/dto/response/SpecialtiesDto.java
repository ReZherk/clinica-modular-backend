package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SpecialtiesDto {
 private Integer id;
 private String nombreEspecialidad;
 private String descripcion;
 private Boolean activo;
}
