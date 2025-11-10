package ReZherk.clinica.sistema.modules.appointment.application.validator;

import ReZherk.clinica.sistema.core.domain.entity.*;
import ReZherk.clinica.sistema.core.domain.repository.*;
import ReZherk.clinica.sistema.core.shared.enums.DiaSemana;
import ReZherk.clinica.sistema.core.shared.enums.EstadoCita;
import ReZherk.clinica.sistema.core.shared.enums.EstadoMedicoHorario;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CitaValidator {

  private final CitaRepository citaRepository;
  private final MedicoHorarioRepository medicoHorarioRepository;
  private final UsuarioRepository usuarioRepository;

  /**
   * Validar creación de cita
   */
  public void validateCrearCita(Integer idMedicoHorario, Integer idPaciente, LocalDate fecha, LocalTime hora) {

    MedicoHorario medicoHorario = medicoHorarioRepository.findById(idMedicoHorario)
        .orElseThrow(() -> new ResourceNotFoundException("Médico horario no encontrado"));

    if (medicoHorario.getEstado() != EstadoMedicoHorario.ACTIVO) {
      throw new BusinessException("El horario del médico no está activo");
    }

    validateUsuarioEsMedico(medicoHorario.getMedico().getId());

    Usuario paciente = usuarioRepository.findById(idPaciente)
        .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

    if (!paciente.getEstadoRegistro()) {
      throw new BusinessException("El paciente no está activo");
    }

    if (fecha.isBefore(LocalDate.now())) {
      throw new ValidationException("La fecha de la cita debe ser futura");
    }

    validarDiaSemana(fecha, medicoHorario.getHorario());

    validarHoraDentroRango(hora, medicoHorario.getHorario());

    // Validar si existe horario ya ocupado
    if (citaRepository.existsCitaByMedicoHorarioAndFechaAndHora(idMedicoHorario, fecha, hora)) {
      throw new BusinessException("El horario seleccionado ya está ocupado");
    }

    // Validar que el paciente no tenga otra cita a la misma hora
    if (citaRepository.existsCitaByPacienteAndFechaAndHora(idPaciente, fecha, hora)) {
      throw new BusinessException("El paciente ya tiene una cita programada en ese horario");
    }
  }

  private Usuario validateUsuarioEsMedico(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado [id=" + id + "]"));

    boolean tieneRolMedicoActivo = usuario.getPerfiles().stream()
        .anyMatch(rol -> "MEDICO".equalsIgnoreCase(rol.getNombre()) && rol.getEstadoRegistro());

    if (!tieneRolMedicoActivo) {
      throw new IllegalStateException("El usuario [id=" + id + "] no tiene rol MEDICO activo");
    }

    return usuario;
  }

  /**
   * Validar cancelación de cita
   */
  public Cita validateCancelarCita(Integer idCita, Integer idUsuario) {
    Cita cita = citaRepository.findByIdWithDetails(idCita)
        .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

    if (cita.getEstado() == EstadoCita.CANCELADA) {
      throw new BusinessException("La cita ya está cancelada");
    }

    if (cita.getEstado() == EstadoCita.COMPLETADA) {
      throw new BusinessException("No se puede cancelar una cita completada");
    }

    if (!cita.getPaciente().getId().equals(idUsuario)) {
      throw new BusinessException(" idUsuario no corresponde a la cita.");
    }

    return cita;
  }

  /**
   * Validar reprogramación de cita
   */
  public void validateReprogramarCita(Cita citaExistente, LocalDate nuevaFecha, LocalTime nuevaHora,
      Integer nuevoIdMedicoHorario) {
    if (citaExistente.getEstado() == EstadoCita.CANCELADA) {
      throw new BusinessException("No se puede reprogramar una cita cancelada");
    }

    if (citaExistente.getEstado() == EstadoCita.COMPLETADA) {
      throw new BusinessException("No se puede reprogramar una cita completada");
    }

    if (nuevaFecha.isBefore(LocalDate.now())) {
      throw new ValidationException("La nueva fecha debe ser futura");
    }

    // Si se cambia el médico, validar que pertenece a la misma especialidad
    if (nuevoIdMedicoHorario != null
        && !nuevoIdMedicoHorario.equals(citaExistente.getMedicoHorario().getIdMedicoHorario())) {
      MedicoHorario nuevoMedicoHorario = medicoHorarioRepository.findById(nuevoIdMedicoHorario)
          .orElseThrow(() -> new ResourceNotFoundException("Nuevo médico horario no encontrado"));

      Integer especialidadActual = citaExistente.getMedicoHorario().getMedico().getMedicoDetalle().getEspecialidad()
          .getId();
      Integer especialidadNueva = nuevoMedicoHorario.getMedico().getMedicoDetalle().getEspecialidad().getId();

      if (!especialidadActual.equals(especialidadNueva)) {
        throw new BusinessException("El nuevo médico debe pertenecer a la misma especialidad");
      }

      // Validar día y hora
      validarDiaSemana(nuevaFecha, nuevoMedicoHorario.getHorario());
      validarHoraDentroRango(nuevaHora, nuevoMedicoHorario.getHorario());

      // Validar disponibilidad
      if (citaRepository.existsCitaByMedicoHorarioAndFechaAndHora(nuevoIdMedicoHorario, nuevaFecha, nuevaHora)) {
        throw new BusinessException("El nuevo horario seleccionado ya está ocupado");
      }
    } else {
      // Mismo médico, validar día y hora
      validarDiaSemana(nuevaFecha, citaExistente.getMedicoHorario().getHorario());
      validarHoraDentroRango(nuevaHora, citaExistente.getMedicoHorario().getHorario());

      // Validar disponibilidad (excluyendo la cita actual)
      if (citaRepository.existsCitaByMedicoHorarioAndFechaAndHora(
          citaExistente.getMedicoHorario().getIdMedicoHorario(), nuevaFecha, nuevaHora)) {
        throw new BusinessException("El nuevo horario seleccionado ya está ocupado");
      }
    }
  }

  /**
   * Vlidar que el día de la semana coincida con el horario del medico
   */
  private void validarDiaSemana(LocalDate fecha, Horario horario) {
    DayOfWeek dayOfWeek = fecha.getDayOfWeek();
    String diaSemanaStr = dayOfWeek.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));

    diaSemanaStr = diaSemanaStr.substring(0, 1).toUpperCase() + diaSemanaStr.substring(1).toLowerCase();

    DiaSemana diaEnum = DiaSemana.valueOf(diaSemanaStr);

    if (!horario.getDiaSemana().equals(diaEnum)) {
      throw new ValidationException(
          String.format("La fecha seleccionada (%s) no coincide con el día del horario del médico (%s)",
              diaSemanaStr, horario.getDiaSemana()));
    }
  }

  /**
   * Validar que la hora este dentro del rango del horario
   */
  private void validarHoraDentroRango(LocalTime hora, Horario horario) {
    if (hora.isBefore(horario.getHoraInicio()) || hora.isAfter(horario.getHoraFin())) {
      throw new ValidationException(
          String.format("La hora seleccionada debe estar entre %s y %s",
              horario.getHoraInicio(), horario.getHoraFin()));
    }
  }

  /**
   * Validar completar cita
   */
  public Cita validateCompletarCita(Integer idCita) {
    Cita cita = citaRepository.findByIdWithDetails(idCita)
        .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

    if (cita.getEstado() == EstadoCita.CANCELADA) {
      throw new BusinessException("No se puede completar una cita cancelada");
    }

    if (cita.getEstado() == EstadoCita.COMPLETADA) {
      throw new BusinessException("La cita ya está completada");
    }

    return cita;
  }
}