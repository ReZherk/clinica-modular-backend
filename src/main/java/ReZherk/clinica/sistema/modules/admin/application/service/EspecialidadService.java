package ReZherk.clinica.sistema.modules.admin.application.service;

import ReZherk.clinica.sistema.core.domain.entity.Especialidad;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.repository.EspecialidadRepository;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.EspecialidadRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.SpecialtyUpdateDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtyUpdateResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtyWithDoctorsResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.EspecialidadMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EspecialidadService {

  private final EspecialidadRepository especialidadRepository;
  private final MedicoDetalleRepository medicoDetalleRepository;

  @Transactional
  public void crearEspecialidad(EspecialidadRequestDto dto) {
    if (especialidadRepository.existsByNombreEspecialidad(dto.getNombreEspecialidad())) {
      throw new IllegalArgumentException("La especialidad '" + dto.getNombreEspecialidad() + "' ya existe.");
    }

    Especialidad especialidad = Especialidad.builder()
        .nombreEspecialidad(dto.getNombreEspecialidad())
        .descripcion(dto.getDescripcion())
        .tarifa(dto.getTarifa())
        .build();
    especialidadRepository.save(especialidad);

  }

  @Transactional(readOnly = true)
  public List<SpecialtyWithDoctorsResponseDto> listarEspecialidadesConMedicos() {
    List<Especialidad> especialidades = especialidadRepository.findAllByOrderByNombreEspecialidad();

    return especialidades.stream()
        .map(especialidad -> {
          List<MedicoDetalle> medicos = medicoDetalleRepository.findByEspecialidad(especialidad);
          return EspecialidadMapper.toDto(especialidad, medicos);
        })
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public SpecialtyWithDoctorsResponseDto buscarMedicosPorEspecialidad(Integer idEspecialidad) {
    Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
        .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada con id " + idEspecialidad));

    List<MedicoDetalle> medicos = medicoDetalleRepository.findByEspecialidad(especialidad);

    return EspecialidadMapper.toDto(especialidad, medicos);
  }

  @Transactional(readOnly = true)
  public List<SpecialtyResponseDto> listarEspecialidades(Boolean estado) {
    return especialidadRepository.findByEstadoRegistroOrderByNombreEspecialidad(
        estado)
        .stream()
        .map(EspecialidadMapper::toSimpleDto)
        .toList();
  }

  @Transactional
  public SpecialtyUpdateResponseDto activar(Integer id) {
    Especialidad especialidad = especialidadRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con id: " + id));

    especialidad.setEstadoRegistro(true);

    Especialidad saved = especialidadRepository.save(especialidad);

    return EspecialidadMapper.toUpdateResponseDto(saved);
  }

  @Transactional
  public void desactivarEspecialidad(Integer id) {
    Especialidad especialidad = especialidadRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con id: " + id));
    especialidad.setEstadoRegistro(false);
    especialidadRepository.save(especialidad);
  }

  @Transactional
  public SpecialtyUpdateResponseDto actualizar(Integer id, SpecialtyUpdateDto dto) {
    Especialidad especialidad = especialidadRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con id: " + id));

    EspecialidadMapper.updateEntity(especialidad, dto);

    Especialidad saved = especialidadRepository.save(especialidad);

    return EspecialidadMapper.toUpdateResponseDto(saved);
  }

  public SpecialtyResponseDto obtenerEspecialidadPorId(Integer id) {

    Especialidad especialidad = especialidadRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Especialidad con ID " + id + " no encontrada"));

    return EspecialidadMapper.toSimpleDto(especialidad);

  }

}
