package ReZherk.clinica.sistema.modules.appointment.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorariosDisponiblesResponseDto {

 private Integer idMedico;
 private String nombreMedico;
 private String especialidad;
 private List<HorarioDisponible> horariosDisponibles;

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public static class HorarioDisponible {
  private Integer idMedicoHorario;
  private LocalTime hora;
  private Boolean disponible; // true = libre, false = ocupado
 }
}