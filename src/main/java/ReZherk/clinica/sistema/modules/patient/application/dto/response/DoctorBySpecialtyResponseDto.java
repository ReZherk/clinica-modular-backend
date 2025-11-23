package ReZherk.clinica.sistema.modules.patient.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorBySpecialtyResponseDto {
 private Integer id;
 private String nombresCompleto;
 private String cmp;
 private String especialidad;
 private boolean estadoRegistro;
}
