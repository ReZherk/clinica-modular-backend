package ReZherk.clinica.sistema.modules.future_modules.service;

import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.future_modules.mapper.RolPerfilMapper;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.CreateRolPerfilDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RolPerfilDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolPerfilService {

  private final RolPerfilRepository rolPerfilRepository;
  private final RolPerfilMapper rolPerfilMapper;

  @Transactional
  public RolPerfilDto createRol(CreateRolPerfilDto createDto) {
    // Verificar si ya existe un rol con ese nombre
    if (rolPerfilRepository.existsByNombre(createDto.getNombre())) {
      throw new BusinessException("Ya existe un rol con el nombre: " + createDto.getNombre());
    }

    RolPerfil rolPerfil = rolPerfilMapper.toEntity(createDto);
    RolPerfil savedRol = rolPerfilRepository.save(rolPerfil);

    return rolPerfilMapper.toDto(savedRol);
  }

  @Transactional(readOnly = true)
  public List<RolPerfilDto> getAllActiveRoles() {
    return rolPerfilRepository.findAllActiveOrderByName()
        .stream()
        .map(rolPerfilMapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public RolPerfilDto getRolById(Integer id) {
    RolPerfil rolPerfil = rolPerfilRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + id));

    return rolPerfilMapper.toDto(rolPerfil);
  }

  @Transactional
  public RolPerfilDto updateRol(Integer id, CreateRolPerfilDto updateDto) {
    RolPerfil existingRol = rolPerfilRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + id));

    // Verificar si el nombre ya existe en otro rol
    if (!existingRol.getNombre().equals(updateDto.getNombre()) &&
        rolPerfilRepository.existsByNombre(updateDto.getNombre())) {
      throw new BusinessException("Ya existe un rol con el nombre: " + updateDto.getNombre());
    }

    rolPerfilMapper.updateEntityFromDto(existingRol, updateDto);
    RolPerfil updatedRol = rolPerfilRepository.save(existingRol);

    return rolPerfilMapper.toDto(updatedRol);
  }

  @Transactional
  public void deleteRol(Integer id) {
    RolPerfil rolPerfil = rolPerfilRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + id));

    // Soft delete
    rolPerfil.setEstadoRegistro(false);
    rolPerfilRepository.save(rolPerfil);
  }

  @Transactional
  public void initializeDefaultRoles() {
    String[] defaultRoles = {
        "SUPERADMIN", "ADMIN_MEDICOS", "ADMIN_HORARIOS", "MEDICO", "PACIENTE"
    };

    for (String roleName : defaultRoles) {
      if (!rolPerfilRepository.existsByNombre(roleName)) {
        RolPerfil rol = RolPerfil.builder()
            .nombre(roleName)
            .descripcion("Rol " + roleName + " del sistema")
            .estadoRegistro(true)
            .build();
        rolPerfilRepository.save(rol);
      }
    }
  }
}