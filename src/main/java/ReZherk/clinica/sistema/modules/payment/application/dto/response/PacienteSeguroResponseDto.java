package ReZherk.clinica.sistema.modules.payment.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteSeguroResponseDto {

 private Integer idPacienteSeguro;
 private String nombreSeguro;
 private String numeroPoliza;
 private LocalDate fechaVigenciaInicio;
 private LocalDate fechaVigenciaFin;
 private Boolean estadoActivo;
 private Boolean vigente;
}
