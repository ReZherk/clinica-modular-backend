package ReZherk.clinica.sistema.modules.admin.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.domain.entity.Horario;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.MedicoHorario;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.HorarioRepository;
import ReZherk.clinica.sistema.core.domain.repository.MedicoHorarioRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.enums.EstadoMedicoHorario;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AsignarHorariosRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.HorarioRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.HorarioResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoConHorariosResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoHorarioAsignacionResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.HorarioMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoHorarioMapper;
import ReZherk.clinica.sistema.modules.admin.application.validator.HorarioValidator;
import ReZherk.clinica.sistema.modules.admin.application.validator.MedicoValidator;
import jakarta.validation.ValidationException;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicoHorarioService {

  private final MedicoHorarioRepository medicoHorarioRepository;
  private final MedicoValidator validator;
  private final HorarioRepository horarioRepository;
  private final UsuarioRepository usuarioRepository;
  private final HorarioMapper horarioMapper;
  private final MedicoHorarioMapper medicoHorarioMapper;
  private final HorarioValidator horarioValidator;

  @Transactional(readOnly = true)
  public List<HorarioResponseDto> obtenerHorariosPorTipo(int horasSemanales) {
    List<Horario> horarios = horarioRepository.findAll();

    List<Horario> horariosFiltrados = horarios.stream()
        .filter(h -> {
          long horas = Duration.between(h.getHoraInicio(), h.getHoraFin()).toHours();
          if (horasSemanales == 48) {
            return horas == 9;
          } else if (horasSemanales == 24) {
            return horas == 4;
          } else {
            throw new IllegalArgumentException("Horas semanales inválidas. Solo se permiten 24 o 48.");
          }
        })
        .collect(Collectors.toList());

    return horariosFiltrados.stream()
        .map(h -> horarioMapper.toResponseDto(h))
        .collect(Collectors.toList());
  }

  @Transactional
  public MedicoHorarioAsignacionResponseDto asignarHorarios(AsignarHorariosRequestDto request) {
    log.info("Asignando horarios al médico ID: {}", request.getIdMedico());

    Usuario medico = usuarioRepository.findById(request.getIdMedico())
        .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con ID: " + request.getIdMedico()));

    MedicoDetalle medicoDetalle = validator.validateDetalleDelMedico(request.getIdMedico());

    if (!medicoDetalle.getUsuario().getEstadoRegistro()) {
      throw new BusinessException("El médico está inactivo");
    }

    List<Horario> horarios = horarioRepository.findAllById(request.getHorarios());

    if (horarios.size() != request.getHorarios().size()) {
      throw new ValidationException("Algunos IDs de horarios no existen");
    }

    List<HorarioRequestDto> horariosParaValidar = horarios.stream()
        .map(h -> HorarioRequestDto.builder()
            .diaSemana(h.getDiaSemana().name())
            .horaInicio(h.getHoraInicio().format(DateTimeFormatter.ofPattern("HH:mm")))
            .horaFin(h.getHoraFin().format(DateTimeFormatter.ofPattern("HH:mm")))
            .build())
        .collect(Collectors.toList());

    // Validar solapamientos y que las horas concuerden con sus horas semanales
    horarioValidator.validarSolapamientos(horariosParaValidar);
    horarioValidator.validarHorasSemanales(horarios, request.getHorasSemanales());

    List<MedicoHorario> medicosHorarios = horarios.stream()
        .map(horario -> MedicoHorario.builder()
            .medico(medico)
            .horario(horario)
            .estado(EstadoMedicoHorario.ACTIVO)
            .build())
        .collect(Collectors.toList());

    medicoHorarioRepository.saveAll(medicosHorarios);

    List<HorarioResponseDto> horariosAsignados = horarios.stream()
        .map(horarioMapper::toResponseDto)
        .collect(Collectors.toList());

    log.info("Se asignaron {} horarios al médico {}", horarios.size(), medico.getNombres());

    return MedicoHorarioAsignacionResponseDto.builder()
        .idMedico(medico.getId())
        .nombreCompleto(medico.getNombres() + " " + medico.getApellidos())
        .especialidad(medicoDetalle.getEspecialidad().getNombreEspecialidad())
        .horariosAsignados(horarios.size())
        .horarios(horariosAsignados)
        .mensaje("Horarios asignados exitosamente")
        .build();
  }

  @Transactional
  public MedicoHorarioAsignacionResponseDto modificarHorarios(AsignarHorariosRequestDto request) {
    log.info("Modificando horarios del médico ID: {}", request.getIdMedico());

    Usuario medico = usuarioRepository.findById(request.getIdMedico())
        .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con ID: " + request.getIdMedico()));

    MedicoDetalle medicoDetalle = validator.validateDetalleDelMedico(request.getIdMedico());

    if (!medicoDetalle.getUsuario().getEstadoRegistro()) {
      throw new BusinessException("El médico está inactivo");
    }

    // Validar que el médico tenga horarios asignados previamente
    List<MedicoHorario> horariosActuales = medicoHorarioRepository.findByMedicoIdAndEstadoActivo(request.getIdMedico());
    if (horariosActuales.isEmpty()) {
      throw new BusinessException("El médico no tiene horarios asignados. Use el endpoint de asignación");
    }

    List<Horario> horarios = horarioRepository.findAllById(request.getHorarios());

    if (horarios.size() != request.getHorarios().size()) {
      throw new ValidationException("Algunos IDs de horarios no existen");
    }

    List<HorarioRequestDto> horariosParaValidar = horarios.stream()
        .map(h -> HorarioRequestDto.builder()
            .diaSemana(h.getDiaSemana().name())
            .horaInicio(h.getHoraInicio().format(DateTimeFormatter.ofPattern("HH:mm")))
            .horaFin(h.getHoraFin().format(DateTimeFormatter.ofPattern("HH:mm")))
            .build())
        .collect(Collectors.toList());

    // Validar solapamientos y que las horas concuerden con sus horas semanales
    horarioValidator.validarSolapamientos(horariosParaValidar);
    horarioValidator.validarHorasSemanales(horarios, request.getHorasSemanales());

    // ✅ CAMBIO: Eliminar físicamente los horarios anteriores en lugar de desactivar
    medicoHorarioRepository.deleteAllByMedicoId(request.getIdMedico());
    medicoHorarioRepository.flush(); // Asegurar que se ejecute el DELETE antes del INSERT

    List<MedicoHorario> medicosHorarios = horarios.stream()
        .map(horario -> MedicoHorario.builder()
            .medico(medico)
            .horario(horario)
            .estado(EstadoMedicoHorario.ACTIVO)
            .build())
        .collect(Collectors.toList());

    medicoHorarioRepository.saveAll(medicosHorarios);

    List<HorarioResponseDto> horariosAsignados = horarios.stream()
        .map(horarioMapper::toResponseDto)
        .collect(Collectors.toList());

    log.info("Se modificaron los horarios del médico {}. Total: {}", medico.getNombres(), horarios.size());

    return MedicoHorarioAsignacionResponseDto.builder()
        .idMedico(medico.getId())
        .nombreCompleto(medico.getNombres() + " " + medico.getApellidos())
        .especialidad(medicoDetalle.getEspecialidad().getNombreEspecialidad())
        .horariosAsignados(horarios.size())
        .horarios(horariosAsignados)
        .mensaje("Horarios modificados exitosamente")
        .build();
  }

  @Transactional(readOnly = true)
  public Page<MedicoConHorariosResponseDto> buscarMedicosConHorarios(String nombre, String dni, String cmp,
      String especialidad, Pageable pageable) {
    log.info("Buscando médicos CON horarios - Página: {}, Tamaño: {}, Sort: {}",
        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

    // Extraer información de ordenamiento
    String sortBy = "apellidos"; // default
    String sortDirection = "ASC"; // default

    if (pageable.getSort().isSorted()) {
      Sort.Order order = pageable.getSort().iterator().next();
      sortBy = order.getProperty();
      sortDirection = order.getDirection().name();
    }

    // Llamar al procedimiento almacenado con los parámetros de ordenamiento
    List<Map<String, Object>> resultados = medicoHorarioRepository.buscarMedicosConHorarios(
        nombre,
        dni,
        cmp,
        especialidad,
        pageable.getPageNumber(),
        pageable.getPageSize(),
        sortBy,
        sortDirection);

    if (resultados.isEmpty()) {
      log.info("No se encontraron médicos con horarios");
      return Page.empty(pageable);
    }

    // Mapear resultados
    List<MedicoConHorariosResponseDto> medicos = resultados.stream()
        .map(medicoHorarioMapper::mapFromStoredProcedure)
        .collect(Collectors.toList());

    return new PageImpl<>(medicos, pageable, resultados.size());
  }

  @Transactional(readOnly = true)
  public Page<MedicoConHorariosResponseDto> buscarMedicosSinHorarios(String nombre, String dni, String cmp,
      String especialidad, Pageable pageable) {
    log.info("Buscando médicos SIN horarios - Página: {}, Tamaño: {}", pageable.getPageNumber(),
        pageable.getPageSize());

    // Llamar al procedimiento almacenado
    List<Map<String, Object>> resultados = medicoHorarioRepository.buscarMedicosSinHorarios(
        nombre,
        dni,
        cmp,
        especialidad,
        pageable.getPageNumber(),
        pageable.getPageSize());

    if (resultados.isEmpty()) {
      log.info("No se encontraron médicos sin horarios");
      return Page.empty(pageable);
    }

    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), resultados.size());

    List<Map<String, Object>> usuariosPaginados = resultados.subList(start, end);

    // Mapear resultados
    List<MedicoConHorariosResponseDto> medicos = usuariosPaginados.stream()
        .map(medicoHorarioMapper::mapFromStoredProcedure)
        .collect(Collectors.toList());

    return new PageImpl<>(medicos, pageable, resultados.size());
  }

  @Transactional(readOnly = true)
  public List<HorarioResponseDto> obtenerHorariosPorMedico(Integer idMedico) {
    log.info("Obteniendo horarios del médico ID: {}", idMedico);

    // Verificar que el médico existe
    if (!usuarioRepository.existsById(idMedico)) {
      throw new ResourceNotFoundException("Médico no encontrado con ID: " + idMedico);
    }

    List<MedicoHorario> medicosHorarios = medicoHorarioRepository.findByMedicoIdAndEstadoActivo(idMedico);

    return medicosHorarios.stream()
        .map(mh -> horarioMapper.toResponseDto(mh.getHorario()))
        .collect(Collectors.toList());
  }

  @Transactional
  public void eliminarHorarioMedico(Integer idMedico, Integer idHorario) {
    log.info("Eliminando horario {} del médico {}", idHorario, idMedico);

    MedicoHorario medicoHorario = medicoHorarioRepository.findByMedicoAndHorarioAndEstadoActivo(idMedico, idHorario)
        .orElseThrow(() -> new ResourceNotFoundException(
            "No se encontró el horario asignado al médico"));

    // Desactivar en lugar de eliminar
    medicoHorarioRepository.deactivateById(medicoHorario.getIdMedicoHorario());

    log.info("Horario desactivado exitosamente");
  }

}