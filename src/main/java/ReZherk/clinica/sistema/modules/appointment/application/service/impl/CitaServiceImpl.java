package ReZherk.clinica.sistema.modules.appointment.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.SpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.MedicoService;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCancelRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCreateRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaFiltroRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaReprogramRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.HorariosDisponiblesRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaDto;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class CitaServiceImpl implements CitaService {

  private final CitaRepository citaRepository;
  private final MedicoHorarioRepository medicoHorarioRepository;
  private final UsuarioRepository usuarioRepository;
  private final CitaMapper citaMapper;
  private final CitaValidator citaValidator;
  private final EspecialidadRepository especialidadRepository;
  private final MedicoService medicoService;

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
  public Page<CitaDto> obtenerCitasPacienteConFiltros(
      Integer idPaciente,
      String nombrePaciente,
      LocalDate fecha,
      String estado,
      Integer page,
      Integer size) {

    Pageable pageable = PageRequest.of(page, size);

    EstadoCita estadoEnum = null;
    if (estado != null && !estado.isBlank()) {
      try {
        estadoEnum = EstadoCita.valueOf(estado.toUpperCase());
      } catch (IllegalArgumentException e) {
        log.warn("Estado inválido recibido: {}. Se ignorará el filtro.", estado);
      }
    }

    Page<Cita> citas = citaRepository.findCitasPacienteConFiltros(
        idPaciente,
        (nombrePaciente == null || nombrePaciente.isBlank()) ? null : nombrePaciente,
        fecha,
        estadoEnum,
        pageable);

    return citas.map(citaMapper::toDto);
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

    if (!medico.getEstadoRegistro()) {
      throw new BusinessException("Este medico esta desactivado");
    }

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

      log.info("Fecha enviada: {} → DiaSemana: {}", request.getFecha(), request.getFecha().getDayOfWeek());
      log.info("Horario ID {} → DiaSemana DB: {}", horario.getIdHorario(), horario.getDiaSemana());

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
  @Transactional
  public CitaResponseDto cancelarCitaAdmin(Integer idCita) {
    log.info("Admin cancelando cita ID: {}", idCita);

    Cita cita = citaRepository.findByIdWithDetails(idCita)
        .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

    if (cita.getEstado() == EstadoCita.CANCELADA) {
      throw new BusinessException("La cita ya está cancelada");
    }

    if (cita.getEstado() == EstadoCita.COMPLETADA) {
      throw new BusinessException("No se puede cancelar una cita completada");
    }

    cita.setEstado(EstadoCita.CANCELADA);
    cita = citaRepository.save(cita);

    log.info("Cita cancelada exitosamente por admin");

    return citaMapper.toResponseDto(cita);
  }

  @Override
  @Transactional
  public CitaResponseDto completarCita(Integer idCita) {
    log.info("Completando cita ID: {}", idCita);

    Cita cita = citaValidator.validateCompletarCita(idCita);

    cita.setEstado(EstadoCita.COMPLETADA);
    cita = citaRepository.save(cita);

    log.info("Cita completada exitosamente");

    return citaMapper.toResponseDto(cita);
  }

  @Override
  public CitaResponseDto reprogramarCita(CitaReprogramRequestDto request) {
    log.info("Reprogramando cita ID: {}", request.getIdCita());

    Cita cita = citaRepository.findByIdWithDetails(request.getIdCita())
        .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

    citaValidator.validateReprogramarCita(
        cita,
        request.getNuevaFecha(),
        request.getNuevaHora(),
        request.getNuevoIdMedicoHorario());

    cita.setFecha(request.getNuevaFecha());
    cita.setHora(request.getNuevaHora());

    if (request.getNuevoIdMedicoHorario() != null &&
        !request.getNuevoIdMedicoHorario().equals(cita.getMedicoHorario().getIdMedicoHorario())) {

      MedicoHorario nuevoMedicoHorario = medicoHorarioRepository.findById(request.getNuevoIdMedicoHorario())
          .orElseThrow(() -> new ResourceNotFoundException("Nuevo médico horario no encontrado"));

      cita.setMedicoHorario(nuevoMedicoHorario);
    }

    cita = citaRepository.save(cita);

    log.info("Cita reprogramada exitosamente");

    // Recargar para verificar
    cita = citaRepository.findByIdWithDetails(cita.getIdCita())
        .orElseThrow(() -> new ResourceNotFoundException("Error al recargar cita"));

    return citaMapper.toResponseDto(cita);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CitaListadoResult> listarCitasConFiltros(CitaFiltroRequestDto filtros) {

    List<CitaListadoResult> resultados = citaRepository.listarCitasConFiltros(
        filtros.getCmpMedico(),
        filtros.getDniMedico(),
        filtros.getNombreMedico(),
        filtros.getIdEspecialidad(),
        filtros.getEstado(),
        filtros.getFecha(),
        filtros.getFechaInicio(),
        filtros.getFechaFin(),
        filtros.getPage(),
        filtros.getSize());

    long total = 0;
    if (!resultados.isEmpty() && resultados.get(0).getTotalRegistros() != null) {
      total = resultados.get(0).getTotalRegistros();
    }

    Pageable pageable = PageRequest.of(filtros.getPage(), filtros.getSize());

    return new PageImpl<>(resultados, pageable, total);
  }

  @Override
  @Transactional(readOnly = true)
  public HorariosDisponiblesResponseDto listarHorariosMedicoCompleto(Integer idMedico, LocalDate fecha) {
    log.info("Listando todos los horarios (libres y ocupados) del médico ID: {} para fecha: {}",
        idMedico, fecha);

    Usuario medico = usuarioRepository.findById(idMedico)
        .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado"));

    if (!medico.getEstadoRegistro()) {
      throw new BusinessException("Este medico esta desactivado");
    }

    MedicoDetalle medicoDetalle = medico.getMedicoDetalle();
    if (medicoDetalle == null) {
      throw new BusinessException("El usuario no es un médico");
    }

    // Obtener horarios activos del médico
    List<MedicoHorario> medicoHorarios = medicoHorarioRepository.findByMedicoIdAndEstadoActivo(
        idMedico);

    if (medicoHorarios.isEmpty()) {
      throw new BusinessException("El médico no tiene horarios configurados");
    }

    // Obtener citas ocupadas en esa fecha
    List<Cita> citasOcupadas = citaRepository.findCitasOcupadasByMedicoAndFecha(idMedico, fecha);

    // Generar lista de horarios (libres y ocupados)
    List<HorariosDisponiblesResponseDto.HorarioDisponible> todosLosHorarios = new ArrayList<>();

    for (MedicoHorario mh : medicoHorarios) {
      Horario horario = mh.getHorario();

      if (!esDiaCorrecto(fecha, horario)) {
        continue;
      }

      Byte duracion = medicoDetalle.getEspecialidad().getDuracion();
      LocalTime horaActual = horario.getHoraInicio();

      while (horaActual.isBefore(horario.getHoraFin())) {
        final LocalTime horaFinal = horaActual;

        // Verificar si la hora está ocupada
        boolean estaOcupado = citasOcupadas.stream()
            .anyMatch(cita -> cita.getHora().equals(horaFinal));

        // Agregar TODOS los horarios (libres y ocupados)
        todosLosHorarios.add(HorariosDisponiblesResponseDto.HorarioDisponible.builder()
            .idMedicoHorario(mh.getIdMedicoHorario())
            .hora(horaActual)
            .disponible(!estaOcupado)
            .build());

        horaActual = horaActual.plusMinutes(duracion);
      }
    }

    return HorariosDisponiblesResponseDto.builder()
        .idMedico(medico.getId())
        .nombreMedico(medico.getNombres() + " " + medico.getApellidos())
        .especialidad(medicoDetalle.getEspecialidad().getNombreEspecialidad())
        .horariosDisponibles(todosLosHorarios)
        .build();
  }

  @Override
  @Transactional
  public CitaResponseDto marcarNoAtendida(Integer idCita) {
    log.info("Marcando cita ID: {} como NO_ATENDIDA", idCita);

    Cita cita = citaValidator.validateMarcarNoAtendida(idCita);

    cita.setEstado(EstadoCita.NO_ATENDIDA);
    cita = citaRepository.save(cita);

    log.info("Cita marcada como NO_ATENDIDA exitosamente");

    return citaMapper.toResponseDto(cita);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SpecialtyResponseDto> listarEspecialidades(Boolean estado) {
    return especialidadRepository.findByEstadoRegistroOrderByNombreEspecialidad(estado)
        .stream()
        .map(CitaMapper::toSimpleDto)
        .toList();
  }

  @Override
  public Page<MedicoResponseDto> getMedicos(String search, String searchType, Pageable pageable,
      String especialidad) {
    return medicoService.getActiveMedicos(search, searchType, pageable, especialidad);
  }

  private boolean esDiaCorrecto(LocalDate fecha, Horario horario) {
    String diaSemana = fecha.getDayOfWeek()
        .getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("es", "ES"));

    diaSemana = diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1).toLowerCase();

    return horario.getDiaSemana().name().equals(diaSemana);
  }

}
