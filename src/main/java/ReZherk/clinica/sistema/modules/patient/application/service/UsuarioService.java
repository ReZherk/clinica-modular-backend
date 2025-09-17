package ReZherk.clinica.sistema.modules.patient.application.service;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.*;
import ReZherk.clinica.sistema.core.domain.repository.*;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.future_modules.dto.*;
import ReZherk.clinica.sistema.modules.future_modules.mapper.MedicoDetalleMapperFuture;
import ReZherk.clinica.sistema.modules.future_modules.mapper.UsuarioMapper;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.RegisterPacienteDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.UsuarioResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.mapper.PacienteDetalleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final RolPerfilRepository rolPerfilRepository;
  private final PacienteDetalleRepository pacienteDetalleRepository;
  private final MedicoDetalleRepository medicoDetalleRepository;
  private final EspecialidadRepository especialidadRepository;

  private final UsuarioMapper usuarioMapper;
  private final PacienteDetalleMapper pacienteDetalleMapper;
  private final MedicoDetalleMapperFuture medicoDetalleMapper;

  private final PasswordEncoder passwordEncoder;

  // ===== REGISTRO PÚBLICO (PACIENTE) =====
  @Transactional
  public RegisterResponseDto registerPaciente(RegisterPacienteDto registerDto) {
    // Validaciones
    validateEmailNotExists(registerDto.getEmail());
    validateDniNotExists(registerDto.getPacienteDetalle().getDni());

    // Crear usuario
    Usuario usuario = createUsuarioBase(registerDto);

    // Asignar rol PACIENTE
    assignRoleToUser(usuario, "PACIENTE");

    // Guardar usuario
    Usuario savedUsuario = usuarioRepository.save(usuario);

    // Crear detalle paciente
    PacienteDetalle pacienteDetalle = pacienteDetalleMapper.toEntity(
        registerDto.getPacienteDetalle(), savedUsuario);
    pacienteDetalleRepository.save(pacienteDetalle);

    return usuarioMapper.toRegisterResponse(savedUsuario,
        "Paciente registrado exitosamente");
  }

  // ===== REGISTRO SUPER ADMIN (MÚLTIPLES ROLES) =====
  @Transactional
  public RegisterResponseDto registerUserBySuperAdmin(RegisterSuperAdminDto registerDto) {
    // Validaciones
    validateEmailNotExists(registerDto.getEmail());

    // Crear usuario
    Usuario usuario = createUsuarioBase(registerDto);

    // Asignar rol especificado
    assignRoleToUser(usuario, registerDto.getRol());

    // Guardar usuario
    Usuario savedUsuario = usuarioRepository.save(usuario);

    // Crear detalle según el rol
    createUserDetails(savedUsuario, registerDto);

    return usuarioMapper.toRegisterResponse(savedUsuario,
        "Usuario registrado exitosamente por SuperAdmin");
  }

  // ===== REGISTRO ADMIN MÉDICOS (SOLO MÉDICOS) =====
  @Transactional
  public RegisterResponseDto registerMedicoByAdmin(RegisterMedicoDto registerDto) {
    // Validaciones
    validateEmailNotExists(registerDto.getEmail());
    validateCmpNotExists(registerDto.getMedicoDetalle().getCmp());
    validateEspecialidadExists(registerDto.getMedicoDetalle().getIdEspecialidad());

    // Crear usuario
    Usuario usuario = createUsuarioBase(registerDto);

    // Asignar rol MEDICO
    assignRoleToUser(usuario, "MEDICO");

    // Guardar usuario
    Usuario savedUsuario = usuarioRepository.save(usuario);

    // Crear detalle médico
    MedicoDetalle medicoDetalle = medicoDetalleMapper.toEntity(
        registerDto.getMedicoDetalle(), savedUsuario);
    medicoDetalleRepository.save(medicoDetalle);

    return usuarioMapper.toRegisterResponse(savedUsuario,
        "Médico registrado exitosamente");
  }

  // ===== MÉTODOS AUXILIARES =====
  private Usuario createUsuarioBase(UsuarioBaseDto dto) {
    Usuario usuario = usuarioMapper.toEntity(dto);

    // Generar salt y hash de contraseña
    String salt = generateSalt();
    String hashedPassword = passwordEncoder.encode(dto.getPassword() + salt);

    usuario.setSalt(salt);
    usuario.setPasswordHash(hashedPassword);

    return usuario;
  }

  private void assignRoleToUser(Usuario usuario, String roleName) {
    RolPerfil rol = rolPerfilRepository.findByNombre(roleName)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));

    Set<RolPerfil> roles = new HashSet<>();
    roles.add(rol);
    usuario.setPerfiles(roles);
  }

  private void createUserDetails(Usuario usuario, RegisterSuperAdminDto registerDto) {
    switch (registerDto.getRol()) {
      case "PACIENTE":
        if (registerDto.getPacienteDetalle() != null) {
          validateDniNotExists(registerDto.getPacienteDetalle().getDni());
          PacienteDetalle pacienteDetalle = pacienteDetalleMapper.toEntity(
              registerDto.getPacienteDetalle(), usuario);
          pacienteDetalleRepository.save(pacienteDetalle);
        }
        break;

      case "MEDICO":
        if (registerDto.getMedicoDetalle() != null) {
          validateCmpNotExists(registerDto.getMedicoDetalle().getCmp());
          validateEspecialidadExists(registerDto.getMedicoDetalle().getIdEspecialidad());
          MedicoDetalle medicoDetalle = medicoDetalleMapper.toEntity(
              registerDto.getMedicoDetalle(), usuario);
          medicoDetalleRepository.save(medicoDetalle);
        }
        break;

      case "ADMIN_MEDICOS":
      case "ADMIN_HORARIOS":
      case "SUPERADMIN":
        // Estos roles no necesitan detalles específicos
        break;

      default:
        throw new BusinessException("Rol no válido: " + registerDto.getRol() +
            ". Roles permitidos: PACIENTE, MEDICO, ADMIN_MEDICOS, ADMIN_HORARIOS, SUPERADMIN");
    }
  }

  private String generateSalt() {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  // ===== VALIDACIONES =====
  private void validateEmailNotExists(String email) {
    if (usuarioRepository.existsByEmail(email)) {
      throw new BusinessException("Ya existe un usuario con el email: " + email);
    }
  }

  private void validateDniNotExists(String dni) {
    if (dni != null && pacienteDetalleRepository.existsByDni(dni)) {
      throw new BusinessException("Ya existe un paciente con el DNI: " + dni);
    }
  }

  private void validateCmpNotExists(String cmp) {
    if (medicoDetalleRepository.existsByCmp(cmp)) {
      throw new BusinessException("Ya existe un médico con el CMP: " + cmp);
    }
  }

  private void validateEspecialidadExists(Integer idEspecialidad) {
    if (!especialidadRepository.existsById(idEspecialidad)) {
      throw new ResourceNotFoundException("Especialidad no encontrada con ID: " + idEspecialidad);
    }
  }

  // ===== MÉTODOS ADICIONALES =====
  @Transactional(readOnly = true)
  public UsuarioResponseDto getUserByEmail(String email) {
    Usuario usuario = usuarioRepository.findByEmailWithRoles(email)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

    return usuarioMapper.toResponseDto(usuario);
  }

  @Transactional(readOnly = true)
  public UsuarioResponseDto getUserById(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

    return usuarioMapper.toResponseDto(usuario);
  }

}
