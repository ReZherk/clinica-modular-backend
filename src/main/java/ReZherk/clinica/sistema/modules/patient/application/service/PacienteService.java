package ReZherk.clinica.sistema.modules.patient.application.service;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.PacienteDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.PacienteDetalleRepository;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCancelRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCreateRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.HorariosDisponiblesRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.HorariosDisponiblesResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.service.CitaService;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.RegisterPacienteDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.PatientCreationResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.mapper.PacienteDetalleMapper;
import ReZherk.clinica.sistema.modules.patient.application.mapper.PacienteMapper;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.SeguroResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.service.SeguroService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PacienteService {

  private final UsuarioRepository usuarioRepository;
  private final PacienteDetalleRepository pacienteDetalleRepository;
  private final RolPerfilRepository rolPerfilRepository;
  private final PacienteDetalleMapper pacienteDetalleMapper;
  private final PacienteMapper pacienteMapper;
  private final PasswordEncoder passwordEncoder;
  private final CitaService citaService;
  private final SeguroService seguroService;

  @Transactional
  public RegisterResponseDto registerPaciente(RegisterPacienteDto registerDto) {

    validateEmailNotExists(registerDto.getEmail());
    validateDniNotExists(registerDto.getNumeroDocumento());

    Usuario usuario = createUsuarioBase(registerDto);
    assignRoleToUser(usuario, "PACIENTE");
    Usuario savedUsuario = usuarioRepository.save(usuario);

    PacienteDetalle detalle = pacienteDetalleMapper
        .toEntity(registerDto.getPacienteDetalle(), savedUsuario);
    pacienteDetalleRepository.save(detalle);

    return pacienteMapper.toRegisterResponse(savedUsuario, "Paciente registrado exitosamente");
  }

  @Transactional(readOnly = true)
  public PatientCreationResponseDto getPacienteByEmail(String email) {
    Usuario usuario = usuarioRepository.findByEmailWithRoles(email)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Paciente no encontrado con email: " + email));
    return pacienteMapper.toResponseDto(usuario);
  }

  @Transactional(readOnly = true)
  public PatientCreationResponseDto getPacienteById(Integer id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Paciente no encontrado con ID: " + id));
    return pacienteMapper.toResponseDto(usuario);
  }

  public void cambiarPassword(Integer userId, String actual, String nueva) {

    Usuario usuario = usuarioRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    if (!passwordEncoder.matches(actual, usuario.getPasswordHash())) {
      throw new RuntimeException("La contraseña actual es incorrecta");
    }

    usuario.setPasswordHash(passwordEncoder.encode(nueva));
    usuarioRepository.save(usuario);
  }

  // ============================
  // MÉTODOS AUXILIARES
  // ============================

  private Usuario createUsuarioBase(UsuarioBaseDto dto) {
    // Convertimos RegisterPacienteDto → Usuario
    Usuario usuario = pacienteMapper.toEntity((RegisterPacienteDto) dto);

    // Guardamos la contraseña con BCrypt (el salt se genera internamente)
    usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

    return usuario;
  }

  private void assignRoleToUser(Usuario usuario, String roleName) {
    var rol = rolPerfilRepository.findByNombre(roleName)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));
    usuario.setPerfiles(Set.of(rol));
  }

  private void validateEmailNotExists(String email) {
    if (usuarioRepository.existsByEmail(email)) {
      throw new BusinessException("Ya existe un usuario con el email: " + email);
    }
  }

  private void validateDniNotExists(String numeroDocumento) {
    if (numeroDocumento != null && usuarioRepository.existsByNumeroDocumento(numeroDocumento)) {
      throw new BusinessException("Ya existe un paciente con el DNI: " + numeroDocumento);
    }
  }

  // Apstir de aqui son servicios que vienen del modulo de citas.

  @Transactional
  public CitaResponseDto crearCita(CitaCreateRequestDto request) {
    return citaService.crearCitaPaciente(request);
  }

  @Transactional
  public CitaResponseDto cancelarCita(CitaCancelRequestDto request) {
    return citaService.cancelarCitaPaciente(request);
  }

  @Transactional(readOnly = true)
  public Page<CitaResponseDto> listarCitas(Integer idPaciente, Integer page, Integer size) {
    return citaService.obtenerCitasPaciente(idPaciente, page, size);
  }

  @Transactional(readOnly = true)
  public CitaResponseDto detalleCita(Integer idCita, Integer idPaciente) {
    return citaService.obtenerDetalleCitaPaciente(idCita, idPaciente);
  }

  @Transactional(readOnly = true)
  public HorariosDisponiblesResponseDto listarHorariosDisponibles(HorariosDisponiblesRequestDto request) {
    return citaService.listarHorariosDisponibles(request);
  }

  // Apartir de aqui son del modulo de pagos

  @Transactional(readOnly = true)
  public List<SeguroResponseDto> listarSegurosConConvenio() {
    return seguroService.listarSegurosConConvenio();
  }

}
