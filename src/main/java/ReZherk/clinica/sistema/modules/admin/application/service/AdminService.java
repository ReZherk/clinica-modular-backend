package ReZherk.clinica.sistema.modules.admin.application.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.entity.UsuarioPerfil;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignAdminToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.ChangePasswordRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.RegisterMedicoDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminBaseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.CountResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.AdminMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.AssignRoleMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoDetalleMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

  private final MedicoValidator validator;
  private final MedicoMapper medicoMapper;
  private final PasswordEncoder passwordEncoder;
  private final RolPerfilRepository rolPerfilRepository;
  private final UsuarioRepository usuarioRepository;
  private final MedicoDetalleMapper medicoDetalleMapper;
  private final MedicoDetalleRepository medicoDetalleRepository;
  private final AssignRoleMapper assignRoleMapper;
  private final UsuarioPerfilRepository usuarioPerfilRepository;

  @Transactional
  public RegisterResponseDto registrarMedico(RegisterMedicoDto registerDto) {
    validator.validateEmailNotExists(registerDto.getEmail());
    validator.validateCmpNotExists(registerDto.getMedicoDetalle().getCmp());
    validator.validateDniNotExists(registerDto.getNumeroDocumento());

    Usuario medico = createUsuarioBase(registerDto);
    assignRoleToUser(medico, "MEDICO");
    Usuario savedMedico = usuarioRepository.save(medico);

    MedicoDetalle medicoDetalle = medicoDetalleMapper.toEntity(registerDto.getMedicoDetalle(), savedMedico);
    medicoDetalleRepository.save(medicoDetalle);

    return medicoMapper.toRegisterResponse(savedMedico, "Medico registrado correctamente");

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

  private Usuario createUsuarioBase(UsuarioBaseDto dto) {
    Usuario user = assignRoleMapper.toEntity(dto);

    // BCrypt maneja internamente el salt, no necesitas generarlo
    String hashedPassword = passwordEncoder.encode(dto.getPassword());
    user.setPasswordHash(hashedPassword);

    return user;
  }

  private void assignRoleToUser(Usuario usuario, String roleName) {
    RolPerfil rol = rolPerfilRepository.findByNombre(roleName)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));

    Set<RolPerfil> roles = new HashSet<>();
    roles.add(rol);
    usuario.setPerfiles(roles);
  }

  // Apartir de qui esta bien,arriab debo modificar.

  @Transactional(readOnly = true)
  public Page<AdminResponseDto> getActiveAdministrators(String search, String searchType, Pageable pageable) {
    log.info("Obteniendo administradores activos - Búsqueda: '{}', Tipo: '{}', Página: {}, Tamaño: {}",
        search != null ? search : "sin filtro", searchType, pageable.getPageNumber(), pageable.getPageSize());

    try {
      Page<AdminResponseDto> result = usuarioRepository
          .findAdministradorsByEstadoAndSearch(true, "ADMINISTRADOR", search, searchType, pageable)
          .map(u -> AdminMapper.toDTO(u));

      log.info("Se encontraron {} administradores activos en total,mostrando {} registros", result.getTotalElements(),
          result.getNumberOfElements());

      return result;
    } catch (Exception e) {
      log.error("Error al obtener administradores activos con busqueda '{}'", search, e);
      throw e;
    }

  }

  @Transactional(readOnly = true)
  public Page<AdminResponseDto> getInactiveAdministrators(String search, String searchType, Pageable pageable) {

    log.info("Obteniendo administradores inactivos - busqueda: '{}' , pagina: '{}' ,Tamaño: '{}'",
        search != null ? search : "Sin filtros", pageable.getPageNumber(), pageable.getPageSize());

    try {

      Page<AdminResponseDto> result = usuarioRepository
          .findAdministradorsByEstadoAndSearch(false, "ADMINISTRADOR", search,
              searchType, pageable)
          .map(U -> AdminMapper.toDTO(U));
      log.info("Se encontraron {} administradores inactivos en total, mostrando {} registros",
          result.getTotalElements(), result.getNumberOfElements());
      return result;
    } catch (Exception e) {

      log.error("Error al obtener administradores inactivos con busqueda: '{}'", search, e);
      throw e;
    }
  }

  @Transactional
  public AdminResponseDto modificarAdministrador(Integer id, AssignAdminToUserRequestDto dto) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado"));

    usuario.setNumeroDocumento(dto.getNumeroDocumento());
    usuario.setNombres(dto.getNombres());
    usuario.setApellidos(dto.getApellidos());
    usuario.setEmail(dto.getEmail());
    usuario.setTelefono(dto.getTelefono());

    Usuario actualizado = usuarioRepository.save(usuario);
    return AdminMapper.toDTO(actualizado);
  }

  @Transactional
  public void cambiarPassword(Integer id, ChangePasswordRequestDto dto) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Administrador no encontrado"));

    if (!passwordEncoder.matches(dto.getPasswordCurrent(), usuario.getPasswordHash())) {
      throw new BadCredentialsException("La contraseña actual es incorrecta");
    }

    String newPasswordHash = passwordEncoder.encode(dto.getNewPassword());
    usuario.setPasswordHash(newPasswordHash);

    usuarioRepository.save(usuario);

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

  // Rvisar

  public List<MedicoResponseDto> listarMedicos() {
    return usuarioRepository.findAll().stream()
        .filter(u -> u.getPerfiles().stream()
            .anyMatch(p -> p.getNombre().equalsIgnoreCase("MEDICO")))
        .map(u -> {
          // si existe detalle, lo traemos
          MedicoDetalle detalle = medicoDetalleRepository.findByIdUsuarioWithUsuario(u.getId())
              .orElse(null);
          return MedicoMapper.toDto(u, detalle);
        })
        .collect(Collectors.toList());
  }

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

}
