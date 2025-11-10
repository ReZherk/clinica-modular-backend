package ReZherk.clinica.sistema.modules.appointment.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.domain.entity.Cita;
import ReZherk.clinica.sistema.core.domain.entity.Horario;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.MedicoHorario;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.CitaRepository;
import ReZherk.clinica.sistema.core.domain.repository.EspecialidadRepository;
import ReZherk.clinica.sistema.core.domain.repository.MedicoHorarioRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.enums.EstadoCita;
import ReZherk.clinica.sistema.core.shared.enums.EstadoMedicoHorario;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCancelRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCreateRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaFiltroRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaReprogramRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.HorariosDisponiblesRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaListadoResult;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.HorariosDisponiblesResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.mapper.CitaMapper;
import ReZherk.clinica.sistema.modules.appointment.application.service.CitaService;
import ReZherk.clinica.sistema.modules.appointment.application.validator.CitaValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CitaServiceImpl implements CitaService {

 private final CitaRepository citaRepository;
 private final MedicoHorarioRepository medicoHorarioRepository;
 private final UsuarioRepository usuarioRepository;
 private final EspecialidadRepository especialidadRepository;
 private final CitaMapper citaMapper;
 private final CitaValidator citaValidator;

 @Override
 @Transactional
 public CitaResponseDto crearCitaPaciente(CitaCreateRequestDto request) {

  log.info("Creando cita para paciente ID: {}", request.getIdPaciente());

  citaValidator.validateCrearCita(
    request.getIdMedicoHorario(),
    request.getIdPaciente(),
    request.getFecha(),
    request.getHora());

  MedicoHorario medicoHorario = medicoHorarioRepository.findById(request.getIdMedicoHorario())
    .orElseThrow(() -> new ResourceNotFoundException("Médico horario no encontrado"));

  Usuario paciente = usuarioRepository.findById(request.getIdPaciente())
    .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

  Cita cita = citaMapper.toEntity(request, medicoHorario, paciente);
  cita = citaRepository.save(cita);

  log.info("Cita creada exitosamente con ID: {}", cita.getIdCita());

  // Recargar para verificiar la ocrrecta creacion.
  cita = citaRepository.findByIdWithDetails(cita.getIdCita())
    .orElseThrow(() -> new ResourceNotFoundException("Error al recargar cita"));

  return citaMapper.toResponseDto(cita);

 }

 @Override
 @Transactional
 public CitaResponseDto cancelarCitaPaciente(CitaCancelRequestDto request) {
  log.info("Cancelando cita ID: {} por paciente ID: {}", request.getIdCita(), request.getIdUsuario());

  Cita cita = citaValidator.validateCancelarCita(request.getIdCita(), request.getIdUsuario());

  cita.setEstado(EstadoCita.CANCELADA);
  cita = citaRepository.save(cita);

  log.info("Cita cancelada exitosamente");

  return citaMapper.toResponseDto(cita);
 }

 @Override
 @Transactional(readOnly = true)
 public Page<CitaResponseDto> obtenerCitasPaciente(Integer idPaciente, Integer page, Integer size) {
  log.info("Obteniendo citas del paciente ID: {}", idPaciente);

  Pageable pageable = PageRequest.of(page, size);
  Page<Cita> citas = citaRepository.findAllByPacienteId(idPaciente, pageable);

  return citas.map(citaMapper::toResponseDto);
 }

 @Override
 @Transactional(readOnly = true)
 public CitaResponseDto obtenerDetalleCitaPaciente(Integer idCita, Integer idPaciente) {
  log.info("Obteniendo detalle de cita ID: {} para paciente ID: {}", idCita, idPaciente);

  Cita cita = citaRepository.findByIdWithDetails(idCita)
    .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

  if (!cita.getPaciente().getId().equals(idPaciente)) {
   throw new BusinessException("No tiene permisos para ver esta cita");
  }

  return citaMapper.toResponseDto(cita);
 }

 @Override
 @Transactional(readOnly = true)
 public HorariosDisponiblesResponseDto listarHorariosDisponibles(HorariosDisponiblesRequestDto request) {
  log.info("Listando horarios disponibles del médico ID: {} para fecha: {}",
    request.getIdMedico(), request.getFecha());

  Usuario medico = usuarioRepository.findById(request.getIdMedico())
    .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado"));

  MedicoDetalle medicoDetalle = medico.getMedicoDetalle();
  if (medicoDetalle == null) {
   throw new BusinessException("El usuario no es un médico");
  }

  List<MedicoHorario> medicoHorarios = medicoHorarioRepository.findByMedicoIdAndEstadoActivo(
    request.getIdMedico());

  if (medicoHorarios.isEmpty()) {
   throw new BusinessException("El médico no tiene horarios configurados");
  }

  // Obtener citas ocupadas en esa fecha
  List<Cita> citasOcupadas = citaRepository.findCitasOcupadasByMedicoAndFecha(
    request.getIdMedico(), request.getFecha());

  List<HorariosDisponiblesResponseDto.HorarioDisponible> horariosDisponibles = new ArrayList<>();

  for (MedicoHorario mh : medicoHorarios) {
   Horario horario = mh.getHorario();

   // Verificar si el día de la semana coincide
   if (!esDiaCorrecto(request.getFecha(), horario)) {
    continue;
   }

   // Generar intervalos de tiempo según la duración de la especialidad
   Byte duracion = medicoDetalle.getEspecialidad().getDuracion();
   LocalTime horaActual = horario.getHoraInicio();

   while (horaActual.isBefore(horario.getHoraFin())) {
    final LocalTime horaFinal = horaActual;

    // Verificar si la hora está ocupada
    boolean estaOcupado = citasOcupadas.stream()
      .anyMatch(cita -> cita.getHora().equals(horaFinal));

    // Solo agregar si está LIBRE (para pacientes)
    if (!estaOcupado) {
     horariosDisponibles.add(HorariosDisponiblesResponseDto.HorarioDisponible.builder()
       .idMedicoHorario(mh.getIdMedicoHorario())
       .hora(horaActual)
       .disponible(true)
       .build());
    }

    horaActual = horaActual.plusMinutes(duracion);
   }
  }

  return HorariosDisponiblesResponseDto.builder()
    .idMedico(medico.getId())
    .nombreMedico(medico.getNombres() + " " + medico.getApellidos())
    .especialidad(medicoDetalle.getEspecialidad().getNombreEspecialidad())
    .horariosDisponibles(horariosDisponibles)
    .build();
 }

 @Override
 public CitaResponseDto crearCitaAdmin(CitaCreateRequestDto request) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'crearCitaAdmin'");
 }

 @Override
 public CitaResponseDto cancelarCitaAdmin(Integer idCita) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'cancelarCitaAdmin'");
 }

 @Override
 public CitaResponseDto completarCita(Integer idCita) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'completarCita'");
 }

 @Override
 public CitaResponseDto reprogramarCita(CitaReprogramRequestDto request) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'reprogramarCita'");
 }

 @Override
 public Page<CitaListadoResult> listarCitasConFiltros(CitaFiltroRequestDto filtros) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'listarCitasConFiltros'");
 }

 @Override
 public HorariosDisponiblesResponseDto listarHorariosMedicoCompleto(Integer idMedico, LocalDate fecha) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'listarHorariosMedicoCompleto'");
 }

 private boolean esDiaCorrecto(LocalDate fecha, Horario horario) {
  String diaSemana = fecha.getDayOfWeek()
    .getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("es", "ES"));

  diaSemana = diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1).toLowerCase();

  return horario.getDiaSemana().name().equals(diaSemana);
 }

}
