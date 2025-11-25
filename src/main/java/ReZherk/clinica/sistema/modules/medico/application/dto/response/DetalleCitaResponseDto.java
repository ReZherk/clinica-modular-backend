package ReZherk.clinica.sistema.modules.medico.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCitaResponseDto {

 private Integer idDetalleCita;
 private Integer idCita;
 private String diagnostico;
 private String receta;
 private String observaciones;
 private LocalDateTime fechaRegistro;
 private LocalDateTime fechaActualizacion;
}