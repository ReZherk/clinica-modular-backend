package ReZherk.clinica.sistema.modules.appointment.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Proyección utilizada para mapear los resultados del procedimiento almacenado
 * sp_listar_citas.
 * 
 * Spring Data JPA crea dinámicamente una implementación de esta interfaz
 * basada en los alias de columnas devueltos por el procedimiento.
 */
public interface CitaListadoResult {

 // Datos de la cita
 Integer getIdCita();

 LocalDate getFecha();

 LocalTime getHora();

 String getEstado();

 String getMotivo();

 // Datos del paciente
 Integer getIdPaciente();

 String getNombresPaciente();

 String getApellidosPaciente();

 String getDocumentoPaciente();

 String getEmailPaciente();

 String getTelefonoPaciente();

 // Datos del médico
 Integer getIdMedico();

 String getNombresMedico();

 String getApellidosMedico();

 String getCmpMedico();

 // Datos de la especialidad
 Integer getIdEspecialidad();

 String getNombreEspecialidad();

 BigDecimal getTarifa();

 Integer getDuracion();

 // Datos del horario
 String getDiaSemana();

 LocalTime getHoraInicio();

 LocalTime getHoraFin();

 // Campo auxiliar para la paginación
 Long getTotalRegistros();
}
