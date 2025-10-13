package ReZherk.clinica.sistema.modules.admin.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.entity.UsuarioPerfil;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignRoleToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.UserResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.AdminMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.AssignRoleMapper;
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

 @Transactional(readOnly = true)
 public Page<UserResponseDto> getActiveUser(String search, String searchType, Pageable pageable, String rol) {

  if (!rolPerfilRepository.existsByNombre(rol)) {
   throw new RuntimeException("El rol '" + rol + "' no existe");
  }

  log.info("Obteniendo usuarios activos - Búsqueda: '{}', Tipo: '{}', Página: {}, Tamaño: {}, rol: {}",
    search != null ? search : "sin busqueda", searchType, pageable.getPageNumber(), pageable.getPageSize(), rol);

  try {
   Page<UserResponseDto> result = usuarioRepository
     .findUserByEstadoAndSearch(true, rol, search, searchType, pageable)
     .map(u -> AdminMapper.toDTO(u, rol));

   log.info("Se encontraron {} usuarios activos en total,mostrando {} registros", result.getTotalElements(),
     result.getNumberOfElements());

   return result;
  } catch (Exception e) {
   log.error("Error al obtener usuarios activos con busqueda '{}'", search, e);
   throw e;
  }

 }

 @Transactional(readOnly = true)
 public Page<UserResponseDto> getInactiveUser(String search, String searchType, Pageable pageable, String rol) {

  log.info("Obteniendo usuarios inactivos - busqueda: '{}' , pagina: '{}' ,Tamaño: '{}'",
    search != null ? search : "Sin busqueda", pageable.getPageNumber(), pageable.getPageSize());

  try {

   Page<UserResponseDto> result = usuarioRepository
     .findUserByEstadoAndSearch(false, rol, search,
       searchType, pageable)
     .map(U -> AdminMapper.toDTO(U, rol));
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

}
