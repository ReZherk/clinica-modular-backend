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
public class HorarioPorDiaDto {

 private String diaSemana;
 private List<BloqueHorarioDto> bloques;

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public static class BloqueHorarioDto {
  private Integer id;
  private String horaInicio;
  private String horaFin;
 }
}
