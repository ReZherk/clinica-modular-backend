package ReZherk.clinica.sistema.modules.admin.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.application.mapper.AssignRoleMapper;
import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.entity.UsuarioPerfil;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AdminCreationRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.ChangePasswordRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminBaseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.UserResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

  private final PasswordEncoder passwordEncoder;
  private final RolPerfilRepository rolPerfilRepository;
  private final UsuarioRepository usuarioRepository;
  private final AssignRoleMapper assignRoleMapper;
  private final UsuarioPerfilRepository usuarioPerfilRepository;

  @Transactional
  public void createAdminUser(AdminCreationRequestDto dto) {

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

  private Usuario createUsuarioBase(UsuarioBaseDto dto) {
    Usuario user = assignRoleMapper.toEntity(dto);

    // BCrypt maneja internamente el salt, no necesitas generarlo
    String hashedPassword = passwordEncoder.encode(dto.getPassword());
    user.setPasswordHash(hashedPassword);

    return user;
  }

  @Transactional(readOnly = true)
  public Page<UserResponseDto> getActiveAdministrators(String search, String searchType, Pageable pageable) {
    log.info("Obteniendo administradores activos - Búsqueda: '{}', Tipo: '{}', Página: {}, Tamaño: {}",
        search != null ? search : "sin busqueda", searchType, pageable.getPageNumber(), pageable.getPageSize());

    try {
      Page<UserResponseDto> result = usuarioRepository
          .findUserByEstadoAndSearch(true, "ADMINISTRADOR", search, searchType, pageable)
          .map(u -> UserMapper.toDTO(u, "ADMINISTRADOR"));

      log.info("Se encontraron {} administradores activos en total,mostrando {} registros", result.getTotalElements(),
          result.getNumberOfElements());

      return result;
    } catch (Exception e) {
      log.error("Error al obtener administradores activos con busqueda '{}'", search, e);
      throw e;
    }

  }

  @Transactional(readOnly = true)
  public Page<UserResponseDto> getInactiveAdministrators(String search, String searchType, Pageable pageable) {

    log.info("Obteniendo administradores inactivos - busqueda: '{}' , pagina: '{}' ,Tamaño: '{}'",
        search != null ? search : "Sin filtros", pageable.getPageNumber(), pageable.getPageSize());

    try {

      Page<UserResponseDto> result = usuarioRepository
          .findUserByEstadoAndSearch(false, "ADMINISTRADOR", search,
              searchType, pageable)
          .map(U -> UserMapper.toDTO(U, "ADMINISTRADOR"));
      log.info("Se encontraron {} administradores inactivos en total, mostrando {} registros",
          result.getTotalElements(), result.getNumberOfElements());
      return result;
    } catch (Exception e) {

      log.error("Error al obtener administradores inactivos con busqueda: '{}'", search, e);
      throw e;
    }
  }

  @Transactional
  public UserResponseDto modificarAdministrador(Integer id, AdminCreationRequestDto dto) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado"));

    usuario.setNumeroDocumento(dto.getNumeroDocumento());
    usuario.setNombres(dto.getNombres());
    usuario.setApellidos(dto.getApellidos());
    usuario.setEmail(dto.getEmail());
    usuario.setTelefono(dto.getTelefono());

    Usuario actualizado = usuarioRepository.save(usuario);
    return UserMapper.toDTO(actualizado, "ADMINISTRADOR");
  }

  @Transactional
  public void cambiarPassword(Integer id, ChangePasswordRequestDto dto) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado"));

    String newPasswordHash = passwordEncoder.encode(dto.getNewPassword());
    usuario.setPasswordHash(newPasswordHash);

    usuarioRepository.save(usuario);

  }

  public UserResponseDto activarAdministrador(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado"));
    usuario.setEstadoRegistro(true);
    return UserMapper.toDTO(usuarioRepository.save(usuario), "ADMINISTRADOR");
  }

  public UserResponseDto desactivarAdministrador(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado"));
    usuario.setEstadoRegistro(false);
    return UserMapper.toDTO(usuarioRepository.save(usuario), "ADMINISTRADOR");
  }

  public AdminBaseDto obtenerAdminPorId(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .filter(u -> u.getPerfiles().stream()
            .anyMatch(rol -> rol.getNombre().equalsIgnoreCase("ADMINISTRADOR")))
        .orElseThrow(() -> new RuntimeException("Administrador no encontrado con id: " + id));

    return assignRoleMapper.toUserBaseDto(usuario);
  }

}
