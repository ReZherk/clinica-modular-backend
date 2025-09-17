package ReZherk.clinica.sistema.modules.admin.application.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
import ReZherk.clinica.sistema.core.domain.repository.RolPerfilRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.core.shared.utils.SecurityUtils;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.RegisterMedicoDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoDetalleMapper;
import ReZherk.clinica.sistema.modules.admin.application.mapper.MedicoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

 private final MedicoValidator validator;
 private final MedicoMapper medicoMapper;
 private final PasswordEncoder passwordEncoder;
 private final RolPerfilRepository rolPerfilRepository;
 private final UsuarioRepository usuarioRepository;
 private final MedicoDetalleMapper medicoDetalleMapper;
 private final MedicoDetalleRepository medicoDetalleRepository;

 @Transactional
 public RegisterResponseDto registrarMedico(RegisterMedicoDto registerDto) {
  validator.validateEmailNotExists(registerDto.getEmail());
  validator.validateCmpNotExists(registerDto.getMedicoDetalle().getCmp());

  Usuario medico = createUsuarioBase(registerDto);
  assignRoleToUser(medico, "MEDICO");
  Usuario savedMedico = usuarioRepository.save(medico);

  MedicoDetalle medicoDetalle = medicoDetalleMapper.toEntity(registerDto.getMedicoDetalle(), savedMedico);
  medicoDetalleRepository.save(medicoDetalle);

  return medicoMapper.toRegisterResponse(savedMedico, "Medico registrado correctamente");

 }

 private Usuario createUsuarioBase(UsuarioBaseDto dto) {
  Usuario medico = medicoMapper.toEntity(dto);

  String salt = SecurityUtils.generateSalt();
  String hashedPassword = passwordEncoder.encode(dto.getPassword() + salt);

  medico.setSalt(salt);
  medico.setPasswordHash(hashedPassword);

  return medico;
 }

 private void assignRoleToUser(Usuario usuario, String roleName) {
  RolPerfil rol = rolPerfilRepository.findByNombre(roleName)
    .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));

  Set<RolPerfil> roles = new HashSet<>();
  roles.add(rol);
  usuario.setPerfiles(roles);
 }
}
