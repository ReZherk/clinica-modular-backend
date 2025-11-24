package ReZherk.clinica.sistema.modules.patient.application.service;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.PacienteDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
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
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.SpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.service.CitaService;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.PatientDataRequestDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.PatientDataResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.DoctorBySpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.mapper.DoctorBySpecialtyMapper;
import ReZherk.clinica.sistema.modules.patient.application.mapper.PacienteDetalleMapper;
import ReZherk.clinica.sistema.modules.patient.application.mapper.PacienteMapper;
import ReZherk.clinica.sistema.modules.patient.application.validator.PacienteValidator;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoSeguroRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoTarjetaRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoYapeRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.VincularSeguroRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.PacienteSeguroResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.PagoResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.ResumenPagoDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.SeguroResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.service.PagoService;
import ReZherk.clinica.sistema.modules.payment.application.service.SeguroService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PacienteService {

  private final UsuarioRepository usuarioRepository;
  private final PacienteDetalleRepository pacienteDetalleRepository;
  private final MedicoDetalleRepository medicoDetalleRepository;
  private final PacienteValidator validator;
  private final RolPerfilRepository rolPerfilRepository;
  private final PacienteDetalleMapper pacienteDetalleMapper;
  private final PacienteMapper pacienteMapper;
  private final PasswordEncoder passwordEncoder;
  private final CitaService citaService;
  private final SeguroService seguroService;
  private final PagoService pagoService;

  @Transactional
  public PatientDataResponseDto registerPaciente(PatientDataRequestDto registerDto) {

    validateEmailNotExists(registerDto.getEmail());
    validateDniNotExists(registerDto.getNumeroDocumento());

    Usuario usuario = createUsuarioBase(registerDto);
    assignRoleToUser(usuario, "PACIENTE");
    Usuario savedUsuario = usuarioRepository.save(usuario);

    PacienteDetalle detalle = pacienteDetalleMapper
        .toEntity(registerDto.getPacienteDetalle(), savedUsuario);
    pacienteDetalleRepository.save(detalle);

    return pacienteMapper.toRegisterResponse(savedUsuario);
  }

  @Transactional(readOnly = true)
  public PatientDataResponseDto obtenerPaciente(Integer idPaciente) {

    Usuario usuario = usuarioRepository.findById(idPaciente)
        .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

    return pacienteMapper.toRegisterResponse(usuario);
  }

  @Transactional
  public void modificarPaciente(Integer id, PatientDataRequestDto dto) {

    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

    if (!dto.getEmail().equalsIgnoreCase(usuario.getEmail())) {

      validateEmailNotExists(dto.getEmail());
    }

    usuario.setEmail(dto.getEmail());
    usuario.setTelefono(dto.getTelefono());

    Usuario actualizado = usuarioRepository.save(usuario);

    PacienteDetalle detalle = pacienteDetalleMapper
        .toEntity(dto.getPacienteDetalle(), actualizado);
    pacienteDetalleRepository.save(detalle);

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

  @Transactional(readOnly = true)
  public Page<DoctorBySpecialtyResponseDto> getActiveMedicos(Pageable pageable,
      Integer idSpecialty) {

    String especialidad = validator.validateEspecialidadExists(idSpecialty);

    log.info("Obteniendo médicos de ESPECIALIDAD ACTIVA - Especialidad: '{}'", especialidad);

    try {
      // Esta query ya filtra médicos con especialidad activa
      List<Usuario> todosLosUsuarios = usuarioRepository
          .findUserByEstadoAndSearchWithProfiles(
              true,
              "MEDICO",
              especialidad,
              null,
              null);

      if (todosLosUsuarios.isEmpty()) {
        log.info("No se encontraron médicos de la especialidad");
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

      List<DoctorBySpecialtyResponseDto> medicosDto = usuariosPaginados.stream()
          .map(usuario -> {
            MedicoDetalle detalle = detallesMap.get(usuario.getId());
            return DoctorBySpecialtyMapper.toDto(usuario, detalle);
          })
          .toList();

      log.info(" Se encontraron {} médicos activos de la especialidad activa de {} totales",
          medicosDto.size(), todosLosUsuarios.size());

      return new PageImpl<>(medicosDto, pageable, todosLosUsuarios.size());

    } catch (Exception e) {
      log.error(" Error al obtener médicos de la especialidad", e);
      throw e;
    }
  }

  // ============================
  // MÉTODOS AUXILIARES
  // ============================

  private Usuario createUsuarioBase(UsuarioBaseDto dto) {
    // Convertimos RegisterPacienteDto → Usuario
    Usuario usuario = pacienteMapper.toEntity((PatientDataRequestDto) dto);

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
  @Transactional(readOnly = true)
  public List<SpecialtyResponseDto> listarEspecialidades(Boolean estado) {
    return citaService.listarEspecialidades(estado);
  }

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

  @Transactional
  public PacienteSeguroResponseDto vincularSeguro(VincularSeguroRequestDto request) {
    return seguroService.vincularSeguro(request);
  }

  @Transactional
  public PagoResponseDto procesarPagoConTarjeta(PagoTarjetaRequestDto request) {
    return pagoService.procesarPagoConTarjeta(request);
  }

  @Transactional
  public PagoResponseDto procesarPagoConYape(PagoYapeRequestDto request) {
    return pagoService.procesarPagoConYape(request);
  }

  @Transactional
  public PagoResponseDto procesarPagoConSeguro(PagoSeguroRequestDto request) {
    return pagoService.procesarPagoConSeguro(request);
  }

  @Transactional(readOnly = true)
  public ResumenPagoDto obtenerResumenPago(Integer idCita) {
    return pagoService.obtenerResumenPago(idCita);
  }

  @Transactional(readOnly = true)
  public PagoResponseDto obtenerDetallePago(Integer idPago) {
    return pagoService.obtenerDetallePago(idPago);
  }

  @Transactional(readOnly = true)
  public PagoResponseDto obtenerPagoPorCita(Integer idCita) {
    return pagoService.obtenerPagoPorCita(idCita);
  }

  @Transactional(readOnly = true)
  public Page<PagoResponseDto> obtenerHistorialPagos(Integer idPaciente, Integer page, Integer size) {

    return pagoService.obtenerHistorialPagos(idPaciente, page, size);
  }

}
