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
import ReZherk.clinica.sistema.core.domain.repository.EspecialidadRepository;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.service.UsuarioRolService;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.ChangePasswordRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.MedicoCreationDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.CountResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.EspecialidadMapper;
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
  private final EspecialidadRepository especialidadRepository;

  @Transactional
  public void registrarMedico(MedicoCreationDto dto) {
    // Validaciones incluyen verificación de especialidad activa
    validator.validateForCreation(
        dto.getEmail(),
        dto.getNumeroDocumento(),
        dto.getMedicoDetalle().getCmp(),
        dto.getMedicoDetalle().getIdEspecialidad());

    Usuario medico = createUsuarioBase(dto);
    usuarioRolService.assignRoleToUser(medico, "MEDICO");
    Usuario savedMedico = usuarioRepository.save(medico);

    MedicoDetalle medicoDetalle = medicoDetalleMapper.toEntity(dto.getMedicoDetalle(), savedMedico);
    medicoDetalleRepository.save(medicoDetalle);

    log.info(" Médico creado exitosamente con especialidad activa");
  }

  private Usuario createUsuarioBase(UsuarioBaseDto dto) {
    Usuario user = assignRoleMapper.toEntity(dto);
    String hashedPassword = passwordEncoder.encode(dto.getPassword());
    user.setPasswordHash(hashedPassword);
    return user;
  }

  public CountResponse contarMedicosActivosInactivos() {
    List<Usuario> medicos = usuarioRepository.findAll().stream()
        .filter(u -> u.getPerfiles().stream()
            .anyMatch(p -> p.getNombre().equalsIgnoreCase("MEDICO") && p.getEstadoRegistro()))
        .toList();

    long activos = medicos.stream().filter(Usuario::getEstadoRegistro).count();
    long inactivos = medicos.size() - activos;

    return CountResponse.builder()
        .activos(activos)
        .inactivos(inactivos)
        .build();
  }

  @Transactional(readOnly = true)
  public Page<MedicoResponseDto> getActiveMedicos(String search, String searchType, Pageable pageable,
      String especialidad) {

    validator.validateEspecialidadExists(especialidad);

    log.info("Obteniendo médicos activos CON ESPECIALIDAD ACTIVA - Búsqueda: '{}', Tipo: '{}', Especialidad: '{}'",
        search != null ? search : "sin busqueda",
        searchType,
        especialidad != null ? especialidad : "todos");

    try {
      // Esta query ya filtra médicos con especialidad activa
      List<Usuario> todosLosUsuarios = usuarioRepository
          .findUserByEstadoAndSearchWithProfiles(
              true,
              "MEDICO",
              especialidad,
              search,
              searchType);

      if (todosLosUsuarios.isEmpty()) {
        log.info("No se encontraron médicos activos con especialidad activa");
        return Page.empty(pageable);
      }

      // Aplicar paginación manualmente
      int start = (int) pageable.getOffset();
      int end = Math.min((start + pageable.getPageSize()), todosLosUsuarios.size());
      List<Usuario> usuariosPaginados = todosLosUsuarios.subList(start, end);

      List<Integer> usuarioIds = usuariosPaginados.stream()
          .map(Usuario::getId)
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

      log.info(" Se encontraron {} médicos activos con especialidad activa de {} totales",
          medicosDto.size(), todosLosUsuarios.size());

      return new PageImpl<>(medicosDto, pageable, todosLosUsuarios.size());

    } catch (Exception e) {
      log.error("✗ Error al obtener médicos activos con búsqueda '{}'", search, e);
      throw e;
    }
  }

  @Transactional(readOnly = true)
  public Page<MedicoResponseDto> getInactiveMedicos(String search, String searchType, Pageable pageable,
      String especialidad) {

    log.info("Obteniendo médicos inactivos - Búsqueda: '{}', Tipo: '{}', Especialidad: '{}'",
        search != null ? search : "sin busqueda",
        searchType,
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
        log.info("No se encontraron médicos inactivos");
        return Page.empty(pageable);
      }

      // Aplicar paginación manualmente
      int start = (int) pageable.getOffset();
      int end = Math.min((start + pageable.getPageSize()), todosLosUsuarios.size());
      List<Usuario> usuariosPaginados = todosLosUsuarios.subList(start, end);

      List<Integer> usuarioIds = usuariosPaginados.stream()
          .map(Usuario::getId)
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

      log.info("Se encontraron {} médicos inactivos de {} totales",
          medicosDto.size(), todosLosUsuarios.size());

      return new PageImpl<>(medicosDto, pageable, todosLosUsuarios.size());

    } catch (Exception e) {
      log.error(" Error al obtener médicos inactivos con búsqueda '{}'", search, e);
      throw e;
    }
  }

  public MedicoResponseDto obtenerMedicoPorId(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .filter(u -> u.getPerfiles().stream()
            .anyMatch(rol -> rol.getNombre().equalsIgnoreCase("MEDICO") && rol.getEstadoRegistro()))
        .orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id + " o rol inactivo"));

    // validateDetalleDelMedico ya verifica que la especialidad esté activa
    MedicoDetalle detalle = validator.validateDetalleDelMedico(usuario.getId());

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

    // validateEspecialidadExistsReturn ya verifica que esté activa
    Especialidad especialidad = validator.validateEspecialidadExistsReturn(dto.getMedicoDetalle().getIdEspecialidad());

    detalle.setEspecialidad(especialidad);
    medicoDetalleRepository.save(detalle);

    log.info("Médico actualizado con especialidad activa: {}", especialidad.getNombreEspecialidad());

    return MedicoMapper.toDto(actualizado, detalle);
  }

  @Transactional
  public void cambiarPassword(Integer id, ChangePasswordRequestDto dto) {
    Usuario usuario = validator.validateUsuarioEsMedico(id);

    String newPasswordHash = passwordEncoder.encode(dto.getNewPassword());
    usuario.setPasswordHash(newPasswordHash);

    usuarioRepository.save(usuario);
  }

  @Transactional
  public MedicoResponseDto activarMedico(Integer id) {
    Usuario usuario = validator.validateUsuarioEsMedico(id);
    usuario.setEstadoRegistro(true);
    usuarioRepository.save(usuario);

    MedicoDetalle detalle = validator.validateDetalleDelMedico(id);
    return MedicoMapper.toDto(usuario, detalle);
  }

  @Transactional
  public MedicoResponseDto desactivarMedico(Integer id) {
    Usuario usuario = validator.validateUsuarioEsMedico(id);
    usuario.setEstadoRegistro(false);
    usuarioRepository.save(usuario);

    MedicoDetalle detalle = validator.validateDetalleDelMedico(id);
    return MedicoMapper.toDto(usuario, detalle);
  }

  @Transactional(readOnly = true)
  public List<SpecialtyResponseDto> listarEspecialidades(Boolean estado) {
    return especialidadRepository.findByEstadoRegistroOrderByNombreEspecialidad(estado)
        .stream()
        .map(EspecialidadMapper::toSimpleDto)
        .toList();
  }
}