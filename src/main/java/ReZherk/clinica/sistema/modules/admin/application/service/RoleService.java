package ReZherk.clinica.sistema.modules.admin.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.entity.UsuarioPerfil;
import ReZherk.clinica.sistema.core.domain.repository.PermissionRepository;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignAdminToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignRoleToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.RoleRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.PermissionResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RoleResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.AssignRoleMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.PermissionMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.Permission;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RolPerfilRepository rolPerfilRepository;
  private final UsuarioPerfilRepository usuarioPerfilRepository;
  private final RoleMapper roleMapper;
  private final AssignRoleMapper assignRoleMapper;
  private final PasswordEncoder passwordEncoder;
  private final UsuarioRepository usuarioRepository;
  private final PermissionRepository permissionRepository;
  private final PermissionMapper permissionMapper;

  @Transactional
  public RoleResponseDto createRole(RoleRequestDto dto) {

    if (rolPerfilRepository.existsByNombre(dto.getNombre())) {
      throw new RuntimeException("El rol '" + dto.getNombre() + "' ya existe");
    }

    RolPerfil rol = roleMapper.toEntity(dto);
    Set<Permission> permissions = new HashSet<>();

    for (String actionKey : dto.getPermissionActionKeys()) {
      Permission permission = permissionRepository.findByActionKey(actionKey)
          .orElseThrow(() -> new RuntimeException(" Permiso no encontrado: " + actionKey));
      permissions.add(permission);
    }
    rol.setPermisos(permissions);

    RolPerfil rolGuardado = rolPerfilRepository.save(rol);

    return roleMapper.toDto(rolGuardado);
  }

  @Transactional
  public void createAdminUser(AssignAdminToUserRequestDto dto) {

    Usuario user = createUsuarioBase(dto);

    Usuario savedUser = usuarioRepository.save(user);

    RolPerfil adminRole = rolPerfilRepository.findByNombre("ADMINISTRADOR")
        .orElseThrow(() -> new ResourceNotFoundException("Rol ADMINISTRADOR no encontrado"));

    UsuarioPerfil link = UsuarioPerfil.builder()
        .idUsuario(savedUser.getId())
        .idPerfil(adminRole.getId())
        .build();

    usuarioPerfilRepository.save(link);
  }

  @Transactional
  public void assignRoleToUser(AssignRoleToUserRequestDto dto) {

    Usuario user = createUsuarioBase(dto);

    Usuario savedUser = usuarioRepository.save(user);

    RolPerfil rol = rolPerfilRepository.findById(dto.getIdRol())
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

    UsuarioPerfil link = UsuarioPerfil.builder()
        .idUsuario(savedUser.getId())
        .idPerfil(rol.getId())
        .build();

    usuarioPerfilRepository.save(link);
  }

  private Usuario createUsuarioBase(UsuarioBaseDto dto) {
    Usuario user = assignRoleMapper.toEntity(dto);

    // BCrypt maneja internamente el salt, no necesitas generarlo
    String hashedPassword = passwordEncoder.encode(dto.getPassword());
    user.setPasswordHash(hashedPassword);

    return user;
  }

  public List<RoleResponseDto> getAllRoles() {
    return roleMapper.toDtoList(rolPerfilRepository.findAll());
  }

  public List<PermissionResponseDto> getAllPermissions() {
    List<Permission> permissions = permissionRepository.findAll();
    return permissionMapper.toDtoList(permissions);
  }

  // Nota:Revisar y hacer que este servicio no crezca mas.
  @Transactional
  public RoleResponseDto deactivateRole(Integer id) {
    RolPerfil rol = rolPerfilRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

    rol.setEstadoRegistro(false);
    rolPerfilRepository.save(rol);

    return roleMapper.toDto(rol);
  }

  @Transactional
  public RoleResponseDto activateRole(Integer id) {
    RolPerfil rol = rolPerfilRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

    rol.setEstadoRegistro(true);
    rolPerfilRepository.save(rol);

    return roleMapper.toDto(rol);
  }

  @Transactional
  public RoleResponseDto updateRole(Integer id, RoleRequestDto dto) {
    RolPerfil rol = rolPerfilRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

    rol.setNombre(dto.getNombre());
    rol.setDescripcion(dto.getDescripcion());

    // actualizar permisos
    Set<Permission> permisos = new HashSet<>();
    for (String actionKey : dto.getPermissionActionKeys()) {
      Permission permiso = permissionRepository.findByActionKey(actionKey)
          .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + actionKey));
      permisos.add(permiso);
    }
    rol.setPermisos(permisos);

    rolPerfilRepository.save(rol);

    return roleMapper.toDto(rol);
  }

}
