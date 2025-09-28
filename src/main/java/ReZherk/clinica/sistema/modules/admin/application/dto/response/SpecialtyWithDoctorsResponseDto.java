package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecialtyWithDoctorsResponseDto {

 private Integer id;
 private String nombreEspecialidad;
 private String descripcion;
 private List<DoctorDto> medicosActivos;
 private List<DoctorDto> medicosInactivos;

}
