package ReZherk.clinica.sistema.modules.admin.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.UserResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

 private final UsuarioRepository usuarioRepository;
 private final RolPerfilRepository rolPerfilRepository;

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

}
