package ReZherk.clinica.sistema.modules.admin.application.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.RegisterMedicoDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.CountResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.AssignRoleMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoDetalleMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicoService {

 private final MedicoValidator validator;
 private final MedicoMapper medicoMapper;
 private final MedicoDetalleMapper medicoDetalleMapper;
 private final AssignRoleMapper assignRoleMapper;
 private final PasswordEncoder passwordEncoder;
 private final RolPerfilRepository rolPerfilRepository;
 private final UsuarioRepository usuarioRepository;
 private final MedicoDetalleRepository medicoDetalleRepository;

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

 private void assignRoleToUser(Usuario usuario, String roleName) {
  RolPerfil rol = rolPerfilRepository.findByNombre(roleName)
    .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));

  Set<RolPerfil> roles = new HashSet<>();
  roles.add(rol);
  usuario.setPerfiles(roles);
 }

 private Usuario createUsuarioBase(UsuarioBaseDto dto) {
  Usuario user = assignRoleMapper.toEntity(dto);

  // BCrypt maneja internamente el salt, no necesitas generarlo
  String hashedPassword = passwordEncoder.encode(dto.getPassword());
  user.setPasswordHash(hashedPassword);

  return user;
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
}
