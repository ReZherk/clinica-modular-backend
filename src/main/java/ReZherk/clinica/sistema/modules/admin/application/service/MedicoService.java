package ReZherk.clinica.sistema.modules.admin.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.service.UsuarioRolService;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.MedicoCreationDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.CountResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.AssignRoleMapper;
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
