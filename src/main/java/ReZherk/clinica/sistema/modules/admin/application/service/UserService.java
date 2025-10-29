package ReZherk.clinica.sistema.modules.admin.application.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.application.mapper.AssignRoleMapper;
import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.entity.UsuarioPerfil;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.core.shared.service.UsuarioRolService;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignRoleToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.ChangePasswordRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.UserResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.UsuarioWithRoleResponse;
import ReZherk.clinica.sistema.modules.admin.application.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UsuarioRepository usuarioRepository;
  private final RolPerfilRepository rolPerfilRepository;
  private final AssignRoleMapper assignRoleMapper;
  private final PasswordEncoder passwordEncoder;
  private final UsuarioPerfilRepository usuarioPerfilRepository;
  private final UsuarioRolService usuarioRolService;

  @Transactional(readOnly = true)
  public Page<UserResponseDto> getActiveUser(String search, String searchType, Pageable pageable, String rol) {
    // Validar rol si se especificó
    if (rol != null && !rol.isEmpty()) {
      RolPerfil rolPerfil = rolPerfilRepository.findByNombre(rol)
          .orElseThrow(() -> new RuntimeException("El rol '" + rol + "' no existe"));

      // Verificar que el rol esté activo
      if (!rolPerfil.getEstadoRegistro()) {
        log.warn("Se intentó filtrar por el rol inactivo: {}", rol);
        throw new RuntimeException("El rol '" + rol + "' está inactivo");
      }
    }

    log.info("Obteniendo usuarios activos CON ROLES ACTIVOS - Búsqueda: '{}', Tipo: '{}', Rol: '{}'",
        search != null ? search : "sin busqueda",
        searchType,
        rol != null ? rol : "todos");

    try {
      Page<UserResponseDto> result = usuarioRepository
          .findAdministrativeUsers(true, rol, search, searchType, pageable)
          .map(u -> UserMapper.toDTO(u, rol));

      log.info("Se encontraron {} usuarios activos con roles activos de {} totales",
          result.getNumberOfElements(), result.getTotalElements());

      return result;
    } catch (Exception e) {
      log.error(" Error al obtener usuarios activos con búsqueda '{}'", search, e);
      throw e;
    }
  }

  @Transactional(readOnly = true)
  public Page<UserResponseDto> getInactiveUser(String search, String searchType, Pageable pageable, String rol) {

    if (rol != null && !rol.isEmpty()) {
      RolPerfil rolPerfil = rolPerfilRepository.findByNombre(rol)
          .orElseThrow(() -> new RuntimeException("El rol '" + rol + "' no existe"));

      if (!rolPerfil.getEstadoRegistro()) {
        log.warn("Se intentó filtrar por el rol inactivo: {}", rol);
        throw new RuntimeException("El rol '" + rol + "' está inactivo");
      }
    }

    log.info("Obteniendo usuarios inactivos - busqueda: '{}' , pagina: '{}' ,Tamaño: '{}'",
        search != null ? search : "Sin busqueda", pageable.getPageNumber(), pageable.getPageSize());

    try {

      Page<UserResponseDto> result = usuarioRepository
          .findAdministrativeUsers(false, rol, search,
              searchType, pageable)
          .map(U -> UserMapper.toDTO(U, rol));
      log.info("Se encontraron {} usuarios inactivos en total, mostrando {} registros",
          result.getTotalElements(), result.getNumberOfElements());
      return result;
    } catch (Exception e) {

      log.error("Error al obtener usuarios inactivos con busqueda: '{}'", search, e);
      throw e;
    }
  }

  @Transactional
  public void assignRoleToUser(AssignRoleToUserRequestDto dto) {

    RolPerfil rol = rolPerfilRepository.findById(dto.getIdRol())
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

    if (!rol.getEstadoRegistro()) {
      throw new IllegalArgumentException(
          "No se puede asignar el rol '" + rol.getNombre() + "' porque está inactivo");
    }

    // Validar que no se intente asignar roles restringidos
    Set<String> rolesRestringidos = Set.of("SUPERADMIN", "ADMINISTRADOR", "MEDICO");

    if (rolesRestringidos.contains(rol.getNombre().toUpperCase())) {
      throw new IllegalArgumentException(
          "No se puede asignar el rol " + rol.getNombre());
    }

    Usuario user = createUsuarioBase(dto);

    Usuario savedUser = usuarioRepository.save(user);

    UsuarioPerfil link = UsuarioPerfil.builder()
        .idUsuario(savedUser.getId())
        .idPerfil(rol.getId())
        .build();

    usuarioPerfilRepository.save(link);
  }

  @Transactional
  private Usuario createUsuarioBase(UsuarioBaseDto dto) {
    Usuario user = assignRoleMapper.toEntity(dto);

    // BCrypt maneja internamente el salt, no necesitas generarlo
    String hashedPassword = passwordEncoder.encode(dto.getPassword());
    user.setPasswordHash(hashedPassword);

    return user;
  }

  public UsuarioWithRoleResponse obtenerUsuarioPorId(Integer id) {

    Usuario usuario = usuarioRepository.findByIdWithActiveRoles(id)
        .orElseGet(() -> {

          log.warn("Usuario {} no tiene roles activos o no existe", id);
          return usuarioRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        });

    String rolActual = usuario.getPerfiles().stream()
        .filter(RolPerfil::getEstadoRegistro)
        .map(RolPerfil::getNombre)
        .findFirst()
        .orElse("SIN ROL ACTIVO");

    UsuarioBaseDto dto = assignRoleMapper.toUserBaseDto(usuario);

    return UsuarioWithRoleResponse.builder()
        .usuario(dto)
        .rolActual(rolActual)
        .build();
  }

  @Transactional
  public UserResponseDto modificarUsuario(Integer id, AssignRoleToUserRequestDto dto) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

    usuario.setNombres(dto.getNombres());
    usuario.setApellidos(dto.getApellidos());
    usuario.setEmail(dto.getEmail());
    usuario.setTelefono(dto.getTelefono());

    if (dto.getIdRol() != null) {
      // Verificar que el nuevo rol esté activo
      RolPerfil nuevoRol = rolPerfilRepository.findById(dto.getIdRol())
          .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

      if (!nuevoRol.getEstadoRegistro()) {
        throw new IllegalArgumentException(
            "No se puede asignar el rol '" + nuevoRol.getNombre() + "' porque está inactivo");
      }

      usuarioRolService.assignRoleToUserById(usuario, dto.getIdRol());
    }

    Usuario actualizado = usuarioRepository.save(usuario);

    String roleName = actualizado.getPerfiles().stream()
        .filter(RolPerfil::getEstadoRegistro)
        .map(RolPerfil::getNombre)
        .findFirst()
        .orElse("SIN_ROL_ACTIVO");

    return UserMapper.toDTO(actualizado, roleName);
  }

  @Transactional
  public void cambiarPassword(Integer id, ChangePasswordRequestDto dto) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado"));

    String newPasswordHash = passwordEncoder.encode(dto.getNewPassword());
    usuario.setPasswordHash(newPasswordHash);

    usuarioRepository.save(usuario);

  }

  @Transactional
  public UserResponseDto activeUser(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

    usuario.setEstadoRegistro(true);
    Usuario usuarioGuardado = usuarioRepository.save(usuario);

    UsuarioWithRoleResponse usuarioConRol = obtenerUsuarioPorId(usuarioGuardado.getId());

    return UserMapper.toDTO(usuarioGuardado, usuarioConRol.getRolActual());
  }

  @Transactional
  public UserResponseDto desactiveUser(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

    usuario.setEstadoRegistro(false);
    Usuario usuarioGuardado = usuarioRepository.save(usuario);

    UsuarioWithRoleResponse usuarioConRol = obtenerUsuarioPorId(usuarioGuardado.getId());

    return UserMapper.toDTO(usuarioGuardado, usuarioConRol.getRolActual());
  }

}
