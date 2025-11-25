package ReZherk.clinica.sistema.modules.medico.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingLinkResponseDto {

 private Integer idMedico;
 private LocalDate fecha;
 private String enlaceReunion;
 private Integer citasActualizadas;
 private String mensaje;

}
