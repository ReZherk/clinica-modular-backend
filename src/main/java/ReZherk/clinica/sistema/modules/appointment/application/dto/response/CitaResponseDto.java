package ReZherk.clinica.sistema.modules.appointment.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ReZherk.clinica.sistema.core.shared.enums.EstadoCita;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaResponseDto {

 private Integer idCita;
 private LocalDate fecha;
 private LocalTime hora;
 private EstadoCita estado;
 private String motivo;
 private LocalDateTime fechaCreacion;
 private LocalDateTime fechaActualizacion;

 // Información del paciente
 private PacienteInfoDto paciente;

 // Información del médico
 private MedicoInfoDto medico;

 // Información de la especialidad
 private EspecialidadInfoDto especialidad;

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

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public static class MedicoInfoDto {
  private Integer id;
  private String nombres;
  private String apellidos;
  private String cmp;
 }

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public static class EspecialidadInfoDto {
  private Integer id;
  private String nombreEspecialidad;
  private BigDecimal tarifa;
  private Byte duracion;
 }
}