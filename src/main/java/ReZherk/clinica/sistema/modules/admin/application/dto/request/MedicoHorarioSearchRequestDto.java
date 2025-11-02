package ReZherk.clinica.sistema.modules.admin.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoHorarioSearchRequestDto {

 private String nombre;
 private String dni;
 private String cmp;
 private String especialidad;

 @Builder.Default
 private Integer page = 0;

 @Builder.Default
 private Integer size = 10;
}
