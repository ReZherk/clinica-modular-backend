package ReZherk.clinica.sistema.modules.admin.application.service;

import ReZherk.clinica.sistema.core.domain.entity.Especialidad;
import ReZherk.clinica.sistema.core.domain.repository.EspecialidadRepository;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.EspecialidadRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EspecialidadService {

 private final EspecialidadRepository especialidadRepository;

 @Transactional
 public Especialidad crearEspecialidad(EspecialidadRequestDto dto) {
  if (especialidadRepository.existsByNombreEspecialidad(dto.getNombreEspecialidad())) {
   throw new IllegalArgumentException("La especialidad '" + dto.getNombreEspecialidad() + "' ya existe.");
  }

  Especialidad especialidad = Especialidad.builder()
    .nombreEspecialidad(dto.getNombreEspecialidad())
    .descripcion(dto.getDescripcion())
    .tarifa(dto.getTarifa())
    .build();

  return especialidadRepository.save(especialidad);
 }
}
