package ReZherk.clinica.sistema.modules.appointment.application.mapper;

import ReZherk.clinica.sistema.core.domain.entity.*;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.SpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCreateRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

  public Cita toEntity(CitaCreateRequestDto dto, MedicoHorario medicoHorario, Usuario paciente) {
    return Cita.builder()
        .medicoHorario(medicoHorario)
        .paciente(paciente)
        .fecha(dto.getFecha())
        .hora(dto.getHora())
        .motivo(dto.getMotivo())
        .build();
  }

  public CitaResponseDto toResponseDto(Cita cita) {
    Usuario medico = cita.getMedicoHorario().getMedico();
    MedicoDetalle medicoDetalle = medico.getMedicoDetalle();
    Especialidad especialidad = medicoDetalle.getEspecialidad();
    Usuario paciente = cita.getPaciente();

    return CitaResponseDto.builder()
        .idCita(cita.getIdCita())
        .fecha(cita.getFecha())
        .hora(cita.getHora())
        .estado(cita.getEstado())
        .motivo(cita.getMotivo())
        .fechaCreacion(cita.getFechaCreacion())
        .fechaActualizacion(cita.getFechaActualizacion())
        .linkReunion(cita.getEnlaceReunion())
        .paciente(CitaResponseDto.PacienteInfoDto.builder()
            .id(paciente.getId())
            .nombres(paciente.getNombres())
            .apellidos(paciente.getApellidos())
            .numeroDocumento(paciente.getNumeroDocumento())
            .email(paciente.getEmail())
            .telefono(paciente.getTelefono())
            .build())
        .medico(CitaResponseDto.MedicoInfoDto.builder()
            .id(medico.getId())
            .nombres(medico.getNombres())
            .apellidos(medico.getApellidos())
            .cmp(medicoDetalle.getCmp())
            .build())
        .especialidad(CitaResponseDto.EspecialidadInfoDto.builder()
            .id(especialidad.getId())
            .nombreEspecialidad(especialidad.getNombreEspecialidad())
            .tarifa(especialidad.getTarifa())
            .duracion(especialidad.getDuracion())
            .build())
        .build();
  }

  public CitaDto toDto(Cita cita) {
    Usuario medico = cita.getMedicoHorario().getMedico();
    MedicoDetalle medicoDetalle = medico.getMedicoDetalle();
    Especialidad especialidad = medicoDetalle.getEspecialidad();
    Usuario paciente = cita.getPaciente();
    DetalleCita detalle = cita.getDetalleCita(); // Puede ser null

    CitaDto.DetalleDto detalleDto = null;
    if (detalle != null) {
      detalleDto = CitaDto.DetalleDto.builder()
          .diagnostico(detalle.getDiagnostico())
          .receta(detalle.getReceta())
          .observaciones(detalle.getObservaciones())
          .build();
    }

    return CitaDto.builder()
        .idCita(cita.getIdCita())
        .fecha(cita.getFecha())
        .hora(cita.getHora())
        .estado(cita.getEstado())
        .motivo(cita.getMotivo())
        .fechaCreacion(cita.getFechaCreacion())
        .fechaActualizacion(cita.getFechaActualizacion())
        .linkReunion(cita.getEnlaceReunion())
        .paciente(CitaDto.PacienteDto.builder()
            .id(paciente.getId())
            .nombres(paciente.getNombres())
            .apellidos(paciente.getApellidos())
            .numeroDocumento(paciente.getNumeroDocumento())
            .email(paciente.getEmail())
            .telefono(paciente.getTelefono())
            .build())
        .medico(CitaDto.MedicoDto.builder()
            .id(medico.getId())
            .nombres(medico.getNombres())
            .apellidos(medico.getApellidos())
            .cmp(medicoDetalle.getCmp())
            .build())
        .especialidad(CitaDto.EspecialidadDto.builder()
            .id(especialidad.getId())
            .nombreEspecialidad(especialidad.getNombreEspecialidad())
            .tarifa(especialidad.getTarifa())
            .duracion(especialidad.getDuracion())
            .build())
        .detalle(detalleDto)
        .build();
  }

  public static SpecialtyResponseDto toSimpleDto(Especialidad especialidad) {
    return SpecialtyResponseDto.builder()
        .id(especialidad.getId())
        .nombreEspecialidad(especialidad.getNombreEspecialidad())
        .descripcion(especialidad.getDescripcion())
        .costo(especialidad.getTarifa())
        .activo(especialidad.getEstadoRegistro())
        .duracion(especialidad.getDuracion())
        .build();
  }
}