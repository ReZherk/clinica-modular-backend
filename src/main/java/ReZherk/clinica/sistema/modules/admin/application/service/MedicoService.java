package ReZherk.clinica.sistema.modules.admin.application.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.application.mapper.AssignRoleMapper;
import ReZherk.clinica.sistema.core.domain.entity.Especialidad;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.service.UsuarioRolService;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.ChangePasswordRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.MedicoCreationDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.CountResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoDetalleMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoMapper;
import ReZherk.clinica.sistema.modules.admin.application.validator.MedicoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicoService {

  private final MedicoValidator validator;
  private final MedicoDetalleMapper medicoDetalleMapper;
  private final AssignRoleMapper assignRoleMapper;
  private final PasswordEncoder passwordEncoder;
  private final UsuarioRepository usuarioRepository;
  private final MedicoDetalleRepository medicoDetalleRepository;
  private final UsuarioRolService usuarioRolService;

  @Transactional
  public void registrarMedico(MedicoCreationDto dto) {
    validator.validateForCreation(dto.getEmail(), dto.getNumeroDocumento(), dto.getMedicoDetalle().getCmp(),
        dto.getMedicoDetalle().getIdEspecialidad());

    Usuario medico = createUsuarioBase(dto);
    usuarioRolService.assignRoleToUser(medico, "MEDICO");
    Usuario savedMedico = usuarioRepository.save(medico);

    MedicoDetalle medicoDetalle = medicoDetalleMapper.toEntity(dto.getMedicoDetalle(), savedMedico);
    medicoDetalleRepository.save(medicoDetalle);

  }

  private Usuario createUsuarioBase(UsuarioBaseDto dto) {
    Usuario user = assignRoleMapper.toEntity(dto);

    // BCrypt maneja internamente el salt, no necesitas generarlo
    String hashedPassword = passwordEncoder.encode(dto.getPassword());
    user.setPasswordHash(hashedPassword);

    return user;
  }

  ////////
  public CountResponse contarMedicosActivosInactivos() {
    List<Usuario> medicos = usuarioRepository.findAll().stream()
        .filter(u -> u.getPerfiles().stream()
            .anyMatch(p -> p.getNombre().equalsIgnoreCase("MEDICO")))
        .toList();

    long activos = medicos.stream().filter(Usuario::getEstadoRegistro).count();
    long inactivos = medicos.size() - activos;

    return CountResponse.builder()
        .activos(activos)
        .inactivos(inactivos)
        .build();
  }

  ////////

  @Transactional(readOnly = true)
  public Page<MedicoResponseDto> getActiveMedicos(String search, String searchType, Pageable pageable,
      String especialidad) {

    validator.validateEspecialidadExists(especialidad);
    log.info("Obteniendo medicos activos - Búsqueda: '{}', Tipo: '{}', Página: {}, Tamaño: {}, Especialidad:{}",
        search != null ? search : "sin busqueda", searchType, pageable.getPageNumber(), pageable.getPageSize(),
        especialidad != null ? especialidad : "todos");

    try {
      List<Usuario> todosLosUsuarios = usuarioRepository
          .findUserByEstadoAndSearchWithProfiles(
              true,
              "MEDICO",
              especialidad,
              search,
              searchType);

      if (todosLosUsuarios.isEmpty()) {
        log.info("No se encontraron médicos activos");
        return Page.empty(pageable);
      }

      // Aplicar paginación manualmente
      int start = (int) pageable.getOffset();
      int end = Math.min((start + pageable.getPageSize()), todosLosUsuarios.size());
      List<Usuario> usuariosPaginados = todosLosUsuarios.subList(start, end);

      List<Integer> usuarioIds = usuariosPaginados.stream()
          .map(u -> u.getId())
          .toList();

      // Cargar TODOS los detalles en UNA SOLA consulta
      List<MedicoDetalle> detalles = medicoDetalleRepository
          .findByUsuarioIdsWithEspecialidad(usuarioIds);

      Map<Integer, MedicoDetalle> detallesMap = detalles.stream()
          .collect(Collectors.toMap(
              d -> d.getUsuario().getId(),
              Function.identity()));

      List<MedicoResponseDto> medicosDto = usuariosPaginados.stream()
          .map(usuario -> {
            MedicoDetalle detalle = detallesMap.get(usuario.getId());
            return MedicoMapper.toDto(usuario, detalle);
          })
          .toList();

      log.info("Se encontraron {} médicos activos en total, mostrando {} registros",
          todosLosUsuarios.size(), medicosDto.size());

      return new PageImpl<>(medicosDto, pageable, todosLosUsuarios.size());

    } catch (Exception e) {
      log.error("Error al obtener médicos activos con búsqueda '{}'", search, e);
      throw e;
    }
  }

  @Transactional(readOnly = true)
  public Page<MedicoResponseDto> getInactiveMedicos(String search, String searchType, Pageable pageable,
      String especialidad) {

    validator.validateEspecialidadExists(especialidad);
    log.info("Obteniendo medicos Inactivos - Búsqueda: '{}', Tipo: '{}', Página: {}, Tamaño: {}, Especialidad:{}",
        search != null ? search : "sin busqueda", searchType, pageable.getPageNumber(), pageable.getPageSize(),
        especialidad != null ? especialidad : "todos");

    try {
      List<Usuario> todosLosUsuarios = usuarioRepository
          .findUserByEstadoAndSearchWithProfiles(
              false,
              "MEDICO",
              especialidad,
              search,
              searchType);

      if (todosLosUsuarios.isEmpty()) {
        log.info("No se encontraron médicos Inactivos");
        return Page.empty(pageable);
      }

      // Aplicar paginación manualmente
      int start = (int) pageable.getOffset();
      int end = Math.min((start + pageable.getPageSize()), todosLosUsuarios.size());
      List<Usuario> usuariosPaginados = todosLosUsuarios.subList(start, end);

      List<Integer> usuarioIds = usuariosPaginados.stream()
          .map(u -> u.getId())
          .toList();

      // Cargar TODOS los detalles en UNA SOLA consulta
      List<MedicoDetalle> detalles = medicoDetalleRepository
          .findByUsuarioIdsWithEspecialidad(usuarioIds);

      Map<Integer, MedicoDetalle> detallesMap = detalles.stream()
          .collect(Collectors.toMap(
              d -> d.getUsuario().getId(),
              Function.identity()));

      List<MedicoResponseDto> medicosDto = usuariosPaginados.stream()
          .map(usuario -> {
            MedicoDetalle detalle = detallesMap.get(usuario.getId());
            return MedicoMapper.toDto(usuario, detalle);
          })
          .toList();

      log.info("Se encontraron {} médicos Inactivos en total, mostrando {} registros",
          todosLosUsuarios.size(), medicosDto.size());

      return new PageImpl<>(medicosDto, pageable, todosLosUsuarios.size());

    } catch (Exception e) {
      log.error("Error al obtener médicos Inactivos con búsqueda '{}'", search, e);
      throw e;
    }
  }

  public MedicoResponseDto obtenerMedicoPorId(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .filter(u -> u.getPerfiles().stream()
            .anyMatch(rol -> rol.getNombre().equalsIgnoreCase("MEDICO")))
        .orElseThrow(() -> new RuntimeException("Medico no encontrado con id: " + id));

    MedicoDetalle detalle = medicoDetalleRepository.findById(usuario.getId())
        .orElseThrow(() -> new RuntimeException("Detalle no encontrado para el médico con id: " + id));

    return MedicoMapper.toDto(usuario, detalle);
  }

  @Transactional
  public MedicoResponseDto modificarMedico(Integer id, MedicoCreationDto dto) {
    Usuario usuario = validator.validateUsuarioEsMedico(id);

    usuario.setNombres(dto.getNombres());
    usuario.setApellidos(dto.getApellidos());
    usuario.setEmail(dto.getEmail());
    usuario.setTelefono(dto.getTelefono());

    Usuario actualizado = usuarioRepository.save(usuario);

    MedicoDetalle detalle = validator.validateDetalleDelMedico(id);

    Especialidad especialidad = validator.validateEspecialidadExistsReturn(dto.getMedicoDetalle().getIdEspecialidad());

    detalle.setEspecialidad(especialidad);

    return MedicoMapper.toDto(actualizado, detalle);
  }

  @Transactional
  public void cambiarPassword(Integer id, ChangePasswordRequestDto dto) {
    Usuario usuario = validator.validateUsuarioEsMedico(id);

    String newPasswordHash = passwordEncoder.encode(dto.getNewPassword());
    usuario.setPasswordHash(newPasswordHash);

    usuarioRepository.save(usuario);

  }

  public MedicoResponseDto activarMedico(Integer id) {
    Usuario usuario = validator.validateUsuarioEsMedico(id);
    usuario.setEstadoRegistro(true);

    MedicoDetalle detalle = validator.validateDetalleDelMedico(id);
    return MedicoMapper.toDto(usuario, detalle);
  }

  public MedicoResponseDto desactivarMedico(Integer id) {
    Usuario usuario = validator.validateUsuarioEsMedico(id);
    usuario.setEstadoRegistro(false);

    MedicoDetalle detalle = validator.validateDetalleDelMedico(id);
    return MedicoMapper.toDto(usuario, detalle);
  }
}
