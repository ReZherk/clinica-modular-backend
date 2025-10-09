package ReZherk.clinica.sistema.modules.patient.application.service;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.PacienteDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.PacienteDetalleRepository;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.RegisterPacienteDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.PatientCreationResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.mapper.PacienteDetalleMapper;
import ReZherk.clinica.sistema.modules.patient.application.mapper.PacienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PacienteService {

  private final UsuarioRepository usuarioRepository;
  private final PacienteDetalleRepository pacienteDetalleRepository;
  private final RolPerfilRepository rolPerfilRepository;
  private final PacienteDetalleMapper pacienteDetalleMapper;
  private final PacienteMapper pacienteMapper;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public RegisterResponseDto registerPaciente(RegisterPacienteDto registerDto) {

    validateEmailNotExists(registerDto.getEmail());
    validateDniNotExists(registerDto.getNumeroDocumento());

    Usuario usuario = createUsuarioBase(registerDto);
    assignRoleToUser(usuario, "PACIENTE");
    Usuario savedUsuario = usuarioRepository.save(usuario);

    PacienteDetalle detalle = pacienteDetalleMapper
        .toEntity(registerDto.getPacienteDetalle(), savedUsuario);
    pacienteDetalleRepository.save(detalle);

    return pacienteMapper.toRegisterResponse(savedUsuario, "Paciente registrado exitosamente");
  }

  @Transactional(readOnly = true)
  public PatientCreationResponseDto getPacienteByEmail(String email) {
    Usuario usuario = usuarioRepository.findByEmailWithRoles(email)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Paciente no encontrado con email: " + email));
    return pacienteMapper.toResponseDto(usuario);
  }

  @Transactional(readOnly = true)
  public PatientCreationResponseDto getPacienteById(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Paciente no encontrado con ID: " + id));
    return pacienteMapper.toResponseDto(usuario);
  }

  // ============================
  // MÉTODOS AUXILIARES
  // ============================

  private Usuario createUsuarioBase(UsuarioBaseDto dto) {
    // Convertimos RegisterPacienteDto → Usuario
    Usuario usuario = pacienteMapper.toEntity((RegisterPacienteDto) dto);

    // Guardamos la contraseña con BCrypt (el salt se genera internamente)
    usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

    return usuario;
  }

  private void assignRoleToUser(Usuario usuario, String roleName) {
    var rol = rolPerfilRepository.findByNombre(roleName)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));
    usuario.setPerfiles(Set.of(rol));
  }

  private void validateEmailNotExists(String email) {
    if (usuarioRepository.existsByEmail(email)) {
      throw new BusinessException("Ya existe un usuario con el email: " + email);
    }
  }

  private void validateDniNotExists(String numeroDocumento) {
    if (numeroDocumento != null && usuarioRepository.existsByNumeroDocumento(numeroDocumento)) {
      throw new BusinessException("Ya existe un paciente con el DNI: " + numeroDocumento);
    }
  }

}
