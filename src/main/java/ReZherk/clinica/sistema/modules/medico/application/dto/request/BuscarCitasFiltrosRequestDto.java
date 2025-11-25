package ReZherk.clinica.sistema.modules.medico.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import ReZherk.clinica.sistema.core.shared.enums.EstadoCita;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarCitasFiltrosRequestDto {

 private String dniPaciente;

 private String nombrePaciente;

 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
 private LocalDate fecha;

 private EstadoCita estado;

 @Builder.Default
 private Integer page = 0;

 @Builder.Default
 private Integer size = 10;
}