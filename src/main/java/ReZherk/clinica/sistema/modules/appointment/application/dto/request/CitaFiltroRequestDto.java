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

 private String cmpMedico; // CMP del médico
 private String dniMedico; // DNI del médico
 private String nombreMedico; // Nombre completo del médico

 private Integer idEspecialidad;
 private String estado; // RESERVADA, CANCELADA, COMPLETADA

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