package ReZherk.clinica.sistema.modules.medico.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import ReZherk.clinica.sistema.core.shared.enums.EstadoCita;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaMedicoResponseDto {

 private Integer idCita;
 private LocalDate fecha;
 private LocalTime hora;
 private EstadoCita estado;
 private String motivo;
 private String enlaceReunion;

 // Informacion del paciente
 private PacienteInfoDto paciente;

 // Detalle de la cita (si existe)
 private DetalleCitaResponseDto detalleCita;

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public static class PacienteInfoDto {
  private Integer id;
  private String nombres;
  private String apellidos;
  private String numeroDocumento;
  private String email;
  private String telefono;
 }
}