package ReZherk.clinica.sistema.service.impl;

import ReZherk.clinica.sistema.dto.UsuarioRequestDTO;
import ReZherk.clinica.sistema.dto.UsuarioResponseDTO;
import ReZherk.clinica.sistema.entity.Usuario;
import ReZherk.clinica.sistema.mapper.UsuarioMapper;
import ReZherk.clinica.sistema.repository.UsuarioRepository;
import ReZherk.clinica.sistema.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

 private final UsuarioRepository usuarioRepository;
 private final UsuarioMapper usuarioMapper;
 private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

 @Override
 public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto) {
  // Convertir DTO → Entidad
  Usuario usuario = usuarioMapper.toEntity(dto);
  // Encriptar password
  usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
  // Guardar
  Usuario saved = usuarioRepository.save(usuario);
  // Devolver DTO
  return usuarioMapper.toResponseDto(saved);
 }

 @Override
 public UsuarioResponseDTO buscarPorEmail(String email) {
  return usuarioRepository.findByEmail(email)
    .map(usuarioMapper::toResponseDto)
    .orElse(null);
 }

 @Override
 public List<UsuarioResponseDTO> listarUsuarios() {
  return usuarioRepository.findAll()
    .stream()
    .map(usuarioMapper::toResponseDto)
    .toList();
 }
}