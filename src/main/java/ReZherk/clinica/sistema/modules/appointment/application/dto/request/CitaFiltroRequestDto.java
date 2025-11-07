package ReZherk.clinica.sistema.modules.appointment.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaFiltroRequestDto {

 private Integer idMedico;
 private Integer idEspecialidad;
 private Integer idPaciente;
 private String estado; // estos: RESERVADA, CANCELADA, COMPLETADA

 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
 private LocalDate fecha;

 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
 private LocalDate fechaInicio;

 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
 private LocalDate fechaFin;

 @Builder.Default
 private Integer page = 0;

 @Builder.Default
 private Integer size = 10;
}