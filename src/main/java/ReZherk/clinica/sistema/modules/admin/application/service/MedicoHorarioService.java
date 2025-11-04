package ReZherk.clinica.sistema.modules.admin.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.domain.entity.Horario;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.MedicoHorario;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.HorarioRepository;
import ReZherk.clinica.sistema.core.domain.repository.MedicoHorarioRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.enums.DiaSemana;
import ReZherk.clinica.sistema.core.shared.enums.EstadoMedicoHorario;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AsignarHorariosRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.HorarioRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.MedicoHorarioSearchRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.HorarioResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoConHorariosResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoHorarioAsignacionResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoHorarioPaginatedResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.HorarioMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoHorarioMapper;
import ReZherk.clinica.sistema.modules.admin.application.validator.HorarioValidator;
import ReZherk.clinica.sistema.modules.admin.application.validator.MedicoValidator;

import java.time.Duration;
import java.util.ArrayList;
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

    horarioValidator.validarHorarios(request.getHorarios());

    Usuario medico = usuarioRepository.findById(request.getIdMedico())
        .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con ID: " + request.getIdMedico()));

    MedicoDetalle medicoDetalle = validator.validateDetalleDelMedico(request.getIdMedico());

    if (!medicoDetalle.getUsuario().getEstadoRegistro()) {
      throw new BusinessException("El médico está inactivo");
    }

    // Desactivar horarios anteriores
    medicoHorarioRepository.deactivateAllByMedicoId(request.getIdMedico());

    List<HorarioResponseDto> horariosAsignados = new ArrayList<>();
    int contadorAsignados = 0;

    for (HorarioRequestDto horarioDto : request.getHorarios()) {
      // Buscar o crear el horario(Debo revisar ya que crea un nuevo horario)
      Horario horario = horarioRepository.findByDiaSemanaAndHoraInicioAndHoraFin(
          DiaSemana.valueOf(horarioDto.getDiaSemana().toUpperCase()),
          java.time.LocalTime.parse(horarioDto.getHoraInicio()),
          java.time.LocalTime.parse(horarioDto.getHoraFin())).orElseGet(() -> {
            Horario nuevoHorario = horarioMapper.toEntity(horarioDto);
            return horarioRepository.save(nuevoHorario);
          });

      // Crear la relación médico-horario
      MedicoHorario medicoHorario = MedicoHorario.builder()
          .medico(medico)
          .horario(horario)
          .estado(EstadoMedicoHorario.ACTIVO)
          .build();

      medicoHorarioRepository.save(medicoHorario);
      horariosAsignados.add(horarioMapper.toResponseDto(horario));
      contadorAsignados++;
    }

    log.info("Se asignaron {} horarios al médico {}", contadorAsignados, medico.getNombres());

    return MedicoHorarioAsignacionResponseDto.builder()
        .idMedico(medico.getId())
        .nombreCompleto(medico.getNombres() + " " + medico.getApellidos())
        .especialidad(medicoDetalle.getEspecialidad().getNombreEspecialidad())
        .horariosAsignados(contadorAsignados)
        .horarios(horariosAsignados)
        .mensaje("Horarios asignados exitosamente")
        .build();
  }

  @Transactional(readOnly = true)
  public MedicoHorarioPaginatedResponseDto buscarMedicosConHorarios(MedicoHorarioSearchRequestDto request) {
    log.info("Buscando médicos CON horarios - Página: {}, Tamaño: {}", request.getPage(), request.getSize());

    // Llamar al procedimiento almacenado para obtener los datos
    List<Map<String, Object>> resultados = medicoHorarioRepository.buscarMedicosConHorarios(
        request.getNombre(),
        request.getDni(),
        request.getCmp(),
        request.getEspecialidad(),
        request.getPage(),
        request.getSize());

    // Obtener el total de registros (segunda consulta del SP)
    Long total = obtenerTotalMedicosConHorarios(request);

    // Mapear resultados
    List<MedicoConHorariosResponseDto> medicos = resultados.stream()
        .map(medicoHorarioMapper::mapFromStoredProcedure)
        .collect(Collectors.toList());

    // Calcular páginas
    int totalPages = (int) Math.ceil((double) total / request.getSize());

    return MedicoHorarioPaginatedResponseDto.builder()
        .content(medicos)
        .currentPage(request.getPage())
        .pageSize(request.getSize())
        .totalElements(total)
        .totalPages(totalPages)
        .hasNext(request.getPage() < totalPages - 1)
        .hasPrevious(request.getPage() > 0)
        .build();
  }

  @Transactional(readOnly = true)
  public MedicoHorarioPaginatedResponseDto buscarMedicosSinHorarios(MedicoHorarioSearchRequestDto request) {
    log.info("Buscando médicos SIN horarios - Página: {}, Tamaño: {}", request.getPage(), request.getSize());

    // Llamar al procedimiento almacenado
    List<Map<String, Object>> resultados = medicoHorarioRepository.buscarMedicosSinHorarios(
        request.getNombre(),
        request.getDni(),
        request.getCmp(),
        request.getEspecialidad(),
        request.getPage(),
        request.getSize());

    // Obtener el total de registros
    Long total = obtenerTotalMedicosSinHorarios(request);

    // Mapear resultados
    List<MedicoConHorariosResponseDto> medicos = resultados.stream()
        .map(medicoHorarioMapper::mapFromStoredProcedure)
        .collect(Collectors.toList());

    // Calcular páginas
    int totalPages = (int) Math.ceil((double) total / request.getSize());

    return MedicoHorarioPaginatedResponseDto.builder()
        .content(medicos)
        .currentPage(request.getPage())
        .pageSize(request.getSize())
        .totalElements(total)
        .totalPages(totalPages)
        .hasNext(request.getPage() < totalPages - 1)
        .hasPrevious(request.getPage() > 0)
        .build();
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

  // Métodos auxiliares para obtener totales
  private Long obtenerTotalMedicosConHorarios(MedicoHorarioSearchRequestDto request) {
    // El SP retorna dos result sets, necesitamos ejecutar una consulta separada
    // para el total
    List<Map<String, Object>> resultados = medicoHorarioRepository.buscarMedicosConHorarios(
        request.getNombre(),
        request.getDni(),
        request.getCmp(),
        request.getEspecialidad(),
        0,
        Integer.MAX_VALUE);

    // Como el SP agrupa por médico, el tamaño de la lista es el total
    return (long) resultados.size();
  }

  private Long obtenerTotalMedicosSinHorarios(MedicoHorarioSearchRequestDto request) {
    List<Map<String, Object>> resultados = medicoHorarioRepository.buscarMedicosSinHorarios(
        request.getNombre(),
        request.getDni(),
        request.getCmp(),
        request.getEspecialidad(),
        0,
        Integer.MAX_VALUE);

    return (long) resultados.size();
  }
}