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
public class MedicoConHorariosResponseDto {

 private Integer idUsuario;
 private String nombres;
 private String apellidos;
 private String nombreCompleto;
 private String dni;
 private String email;
 private String telefono;
 private String cmp;
 private Integer idEspecialidad;
 private String especialidad;
 private Integer duracionConsulta;
 private Integer horasSemanales;
 private List<HorarioPorDiaDto> horariosPorDia;
 private Integer totalBloquesHorarios;
}
