package ReZherk.clinica.sistema.modules.admin.application.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.core.shared.utils.SecurityUtils;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignAdminToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.RegisterMedicoDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminBaseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.AdminMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.AssignRoleMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoDetalleMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final MedicoValidator validator;
  private final MedicoMapper medicoMapper;
  private final PasswordEncoder passwordEncoder;
  private final RolPerfilRepository rolPerfilRepository;
  private final UsuarioRepository usuarioRepository;
  private final MedicoDetalleMapper medicoDetalleMapper;
  private final MedicoDetalleRepository medicoDetalleRepository;
  private final AssignRoleMapper assignRoleMapper;

  @Transactional
  public RegisterResponseDto registrarMedico(RegisterMedicoDto registerDto) {
    validator.validateEmailNotExists(registerDto.getEmail());
    validator.validateCmpNotExists(registerDto.getMedicoDetalle().getCmp());
    validator.validateDniNotExists(registerDto.getDni());

    Usuario medico = createUsuarioBase(registerDto);
    assignRoleToUser(medico, "MEDICO");
    Usuario savedMedico = usuarioRepository.save(medico);

    MedicoDetalle medicoDetalle = medicoDetalleMapper.toEntity(registerDto.getMedicoDetalle(), savedMedico);
    medicoDetalleRepository.save(medicoDetalle);

    return medicoMapper.toRegisterResponse(savedMedico, "Medico registrado correctamente");

  }

  private Usuario createUsuarioBase(UsuarioBaseDto dto) {
    Usuario medico = medicoMapper.toEntity(dto);

    String salt = SecurityUtils.generateSalt();
    String hashedPassword = passwordEncoder.encode(dto.getPassword() + salt);

    medico.setPasswordHash(hashedPassword);

    return medico;
  }

  private void assignRoleToUser(Usuario usuario, String roleName) {
    RolPerfil rol = rolPerfilRepository.findByNombre(roleName)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));

    Set<RolPerfil> roles = new HashSet<>();
    roles.add(rol);
    usuario.setPerfiles(roles);
  }

  // Apartir de qui esta bien,arriab debo modificar.

  public List<AdminResponseDto> listarAdministradores() {
    return usuarioRepository.findAll().stream()
        .filter(u -> u.getPerfiles().stream().anyMatch(p -> p.getNombre().equalsIgnoreCase("ADMINISTRADOR")))
        .map(AdminMapper::toDTO)
        .collect(Collectors.toList());
  }

  public AdminResponseDto modificarAdministrador(Integer id, AssignAdminToUserRequestDto dto) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado"));

    usuario.setDni(dto.getDni());
    usuario.setNombres(dto.getNombres());
    usuario.setApellidos(dto.getApellidos());
    usuario.setEmail(dto.getEmail());
    usuario.setTelefono(dto.getTelefono());

    Usuario actualizado = usuarioRepository.save(usuario);
    return AdminMapper.toDTO(actualizado);
  }

  public AdminResponseDto activarAdministrador(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado"));
    usuario.setEstadoRegistro(true);
    return AdminMapper.toDTO(usuarioRepository.save(usuario));
  }

  public AdminResponseDto desactivarAdministrador(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado"));
    usuario.setEstadoRegistro(false);
    return AdminMapper.toDTO(usuarioRepository.save(usuario));
  }

  public AdminBaseDto obtenerAdminPorId(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .filter(u -> u.getPerfiles().stream()
            .anyMatch(rol -> rol.getNombre().equalsIgnoreCase("ADMINISTRADOR")))
        .orElseThrow(() -> new RuntimeException("Administrador no encontrado con id: " + id));

    return assignRoleMapper.toAdminBaseDto(usuario);
  }

}
