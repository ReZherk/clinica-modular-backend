package ReZherk.clinica.sistema.modules.appointment.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaListadoResult {

 private Integer idCita;
 private LocalDate fecha;
 private LocalTime hora;
 private String estado;

 // Datos del paciente
 private Integer idPaciente;
 private String nombresPaciente;
 private String apellidosPaciente;
 private String documentoPaciente;
 private String emailPaciente;
 private String telefonoPaciente;

 // Datos del médico
 private Integer idMedico;
 private String nombresMedico;
 private String apellidosMedico;
 private String cmpMedico;

 // Datos de la especialidad
 private Integer idEspecialidad;
 private String nombreEspecialidad;
 private BigDecimal tarifa;
 private Byte duracion;

 // Datos del horario
 private String diaSemana;
 private LocalTime horaInicio;
 private LocalTime horaFin;

 private String motivo;
 private Long totalRegistros;
}