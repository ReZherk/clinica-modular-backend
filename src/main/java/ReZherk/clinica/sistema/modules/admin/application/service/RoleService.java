package ReZherk.clinica.sistema.modules.admin.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.entity.UsuarioPerfil;
import ReZherk.clinica.sistema.core.domain.repository.OpcionMenuRepository;
import ReZherk.clinica.sistema.core.domain.repository.PerfilOpcionMenuRepository;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.core.shared.utils.SecurityUtils;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignRoleToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.CreateRoleRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RoleResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.AssignRoleMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.OpcionMenu;
import ReZherk.clinica.sistema.core.domain.entity.PerfilOpcionMenu;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RolPerfilRepository rolPerfilRepository;
  private final UsuarioPerfilRepository usuarioPerfilRepository;
  private final PerfilOpcionMenuRepository perfilOpcionMenuRepository;
  private final OpcionMenuRepository opcionMenuRepository;
  private final RoleMapper roleMapper;
  private final AssignRoleMapper assignRoleMapper;
  private final PasswordEncoder passwordEncoder;
  private final UsuarioRepository usuarioRepository;

  @Transactional
  public RoleResponseDto createRole(CreateRoleRequestDto dto) {

    RolPerfil rol = roleMapper.toEntity(dto);

    RolPerfil rolGuardado = rolPerfilRepository.save(rol);

    // Entra a este if si el rol ya tiene acciones.
    if (dto.getAccionesIds() != null && !dto.getAccionesIds().isEmpty()) {
      List<OpcionMenu> menus = opcionMenuRepository.findAllById(dto.getAccionesIds());

      if (menus.size() != dto.getAccionesIds().size()) {
        throw new ResourceNotFoundException("Una o más acciones no existen en la base de datos");
      }

      List<PerfilOpcionMenu> enlaces = menus.stream()
          .map(menu -> PerfilOpcionMenu.builder()
              .rol(rolGuardado)
              .opcionMenu(menu)
              .build())
          .collect(Collectors.toList());

      perfilOpcionMenuRepository.saveAll(enlaces);
    }

    return roleMapper.toDto(rolGuardado);
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

    String salt = SecurityUtils.generateSalt();
    String hashedPassword = passwordEncoder.encode(dto.getPassword() + salt);

    user.setPasswordHash(hashedPassword);

    return user;
  }

  public List<RoleResponseDto> getAllRoles() {
    return roleMapper.toDtoList(rolPerfilRepository.findAll());
  }

}
