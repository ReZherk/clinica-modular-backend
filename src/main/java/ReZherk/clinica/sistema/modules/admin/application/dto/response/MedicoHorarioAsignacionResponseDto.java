package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoHorarioAsignacionResponseDto {

 private Integer idMedico;
 private String nombreCompleto;
 private String especialidad;
 private Integer horariosAsignados;
 private List<HorarioResponseDto> horarios;
 private String mensaje;
}