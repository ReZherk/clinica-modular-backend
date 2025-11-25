package ReZherk.clinica.sistema.modules.medico.application.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Cita;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.CitaHistorialResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.CitaMedicoResponseDto;

@Component
@RequiredArgsConstructor
public class CitaMedicoMapper {

 private final DetalleCitaMapper detalleCitaMapper;

 public CitaMedicoResponseDto toCitaMedicoResponseDto(Cita cita) {
  Usuario paciente = cita.getPaciente();

  return CitaMedicoResponseDto.builder()
    .idCita(cita.getIdCita())
    .fecha(cita.getFecha())
    .hora(cita.getHora())
    .estado(cita.getEstado())
    .motivo(cita.getMotivo())
    .enlaceReunion(cita.getEnlaceReunion())
    .paciente(CitaMedicoResponseDto.PacienteInfoDto.builder()
      .id(paciente.getId())
      .nombres(paciente.getNombres())
      .apellidos(paciente.getApellidos())
      .numeroDocumento(paciente.getNumeroDocumento())
      .email(paciente.getEmail())
      .telefono(paciente.getTelefono())
      .build())
    .detalleCita(detalleCitaMapper.toResponseDto(cita.getDetalleCita()))
    .build();
 }

 public CitaHistorialResponseDto toCitaHistorialResponseDto(Cita cita) {
  Usuario medico = cita.getMedicoHorario().getMedico();

  return CitaHistorialResponseDto.builder()
    .idCita(cita.getIdCita())
    .fecha(cita.getFecha())
    .hora(cita.getHora())
    .estado(cita.getEstado())
    .motivo(cita.getMotivo())
    .enlaceReunion(cita.getEnlaceReunion())
    .medico(CitaHistorialResponseDto.MedicoInfoDto.builder()
      .id(medico.getId())
      .nombres(medico.getNombres())
      .apellidos(medico.getApellidos())
      .cmp(medico.getMedicoDetalle().getCmp())
      .especialidad(medico.getMedicoDetalle().getEspecialidad().getNombreEspecialidad())
      .build())
    .detalleCita(detalleCitaMapper.toResponseDto(cita.getDetalleCita()))
    .build();
 }
}