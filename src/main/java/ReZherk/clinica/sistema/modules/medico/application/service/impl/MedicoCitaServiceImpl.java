package ReZherk.clinica.sistema.modules.medico.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.domain.entity.Cita;
import ReZherk.clinica.sistema.core.domain.entity.DetalleCita;
import ReZherk.clinica.sistema.core.domain.repository.CitaRepository;
import ReZherk.clinica.sistema.core.domain.repository.DetalleCitaRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.service.CitaService;
import ReZherk.clinica.sistema.modules.medico.application.dto.request.BuscarCitasFiltrosRequestDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.request.DetalleCitaRequestDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.request.MeetingLinkRequestDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.CitaHistorialResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.CitaMedicoResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.DetalleCitaResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.MeetingLinkResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.mapper.CitaMedicoMapper;
import ReZherk.clinica.sistema.modules.medico.application.mapper.DetalleCitaMapper;
import ReZherk.clinica.sistema.modules.medico.application.service.MedicoCitaService;
import ReZherk.clinica.sistema.modules.medico.application.validator.MedicoCitaValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicoCitaServiceImpl implements MedicoCitaService {

 private final CitaRepository citaRepository;
 private final DetalleCitaRepository detalleCitaRepository;
 private final CitaMedicoMapper citaMedicoMapper;
 private final DetalleCitaMapper detalleCitaMapper;
 private final MedicoCitaValidator medicoCitaValidator;
 private final CitaService citaService;

 @Override
 @Transactional(readOnly = true)
 public List<CitaMedicoResponseDto> listarCitasDelDia(Integer idMedico, LocalDate fecha) {
  log.info("Listando citas del medico ID: {} para la fecha: {}", idMedico, fecha);

  // Si no se especifica fecha, usar hoy
  LocalDate fechaBusqueda = fecha != null ? fecha : LocalDate.now();

  List<Cita> citas = citaRepository.findCitasByMedicoAndFecha(idMedico, fechaBusqueda);

  log.info("Se encontraron {} citas para el medico en la fecha {}", citas.size(), fechaBusqueda);

  return citas.stream()
    .map(citaMedicoMapper::toCitaMedicoResponseDto)
    .collect(Collectors.toList());
 }

 @Override
 @Transactional
 public CitaResponseDto cancelarCitaAdmin(Integer idCita) {
  return citaService.cancelarCitaAdmin(idCita);
 }

 @Override
 @Transactional
 public CitaResponseDto completarCita(Integer idCita) {
  return citaService.completarCita(idCita);
 }

 @Override
 @Transactional(readOnly = true)
 public CitaMedicoResponseDto obtenerDetalleCita(Integer idCita) {
  log.info("Obteniendo detalle de cita ID: {}", idCita);

  Cita cita = citaRepository.findByIdWithDetails(idCita)
    .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

  return citaMedicoMapper.toCitaMedicoResponseDto(cita);
 }

 @Override
 @Transactional
 public DetalleCitaResponseDto registrarDetalleCita(DetalleCitaRequestDto request) {
  log.info("Registrando detalle para cita ID: {}", request.getIdCita());

  Cita cita = medicoCitaValidator.validateCitaParaDetalle(request.getIdCita());

  medicoCitaValidator.validateDetalleNoExiste(request.getIdCita());

  DetalleCita detalleCita = detalleCitaMapper.toEntity(request, cita);
  detalleCita = detalleCitaRepository.save(detalleCita);

  log.info("Detalle de cita registrado con ID: {}", detalleCita.getIdDetalleCita());

  return detalleCitaMapper.toResponseDto(detalleCita);
 }

 @Override
 @Transactional
 public DetalleCitaResponseDto actualizarDetalleCita(Integer idDetalleCita, DetalleCitaRequestDto request) {
  log.info("Actualizando detalle de cita ID: {}", idDetalleCita);

  DetalleCita detalleCita = detalleCitaRepository.findById(idDetalleCita)
    .orElseThrow(() -> new ResourceNotFoundException("Detalle de cita no encontrado"));

  // Actualizar campos
  detalleCitaMapper.updateEntity(detalleCita, request);
  detalleCita = detalleCitaRepository.save(detalleCita);

  log.info("Detalle de cita actualizado exitosamente");

  return detalleCitaMapper.toResponseDto(detalleCita);
 }

 @Override
 @Transactional
 public MeetingLinkResponseDto enviarEnlaceReunion(MeetingLinkRequestDto request) {
  log.info("Enviando enlace de reunion para medico ID: {} en fecha: {}",
    request.getIdMedico(), request.getFecha());

  // Validar enlace
  medicoCitaValidator.validateEnlaceReunion(request.getEnlaceReunion());

  // Actualizar todas las citas del medico en esa fecha
  int citasActualizadas = citaRepository.updateEnlaceReunionByMedicoAndFecha(
    request.getIdMedico(),
    request.getFecha(),
    request.getEnlaceReunion());

  log.info("Enlace de reunion actualizado en {} citas", citasActualizadas);

  return MeetingLinkResponseDto.builder()
    .idMedico(request.getIdMedico())
    .fecha(request.getFecha())
    .enlaceReunion(request.getEnlaceReunion())
    .citasActualizadas(citasActualizadas)
    .mensaje(String.format("Enlace enviado a %d citas reservadas", citasActualizadas))
    .build();
 }

 @Override
 @Transactional(readOnly = true)
 public List<CitaHistorialResponseDto> obtenerHistorialPaciente(Integer idPaciente) {
  log.info("Obteniendo historial de citas del paciente ID: {}", idPaciente);

  List<Cita> historial = citaRepository.findHistorialByPacienteId(idPaciente);

  log.info("Se encontraron {} citas en el historial del paciente", historial.size());

  return historial.stream()
    .map(citaMedicoMapper::toCitaHistorialResponseDto)
    .collect(Collectors.toList());
 }

 @Override
 @Transactional(readOnly = true)
 public Page<CitaMedicoResponseDto> buscarCitasConFiltros(BuscarCitasFiltrosRequestDto filtros) {
  log.info("Buscando citas con filtros: DNI={}, Nombre={}, Fecha={}, Estado={}",
    filtros.getDniPaciente(), filtros.getNombrePaciente(),
    filtros.getFecha(), filtros.getEstado());

  Pageable pageable = PageRequest.of(filtros.getPage(), filtros.getSize());

  Page<Cita> citas = citaRepository.findCitasConFiltros(
    filtros.getDniPaciente(),
    filtros.getNombrePaciente(),
    filtros.getFecha(),
    filtros.getEstado(),
    pageable);

  log.info("Se encontraron {} citas con los filtros aplicados", citas.getTotalElements());

  return citas.map(citaMedicoMapper::toCitaMedicoResponseDto);
 }
}