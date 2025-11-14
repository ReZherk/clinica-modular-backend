package ReZherk.clinica.sistema.modules.auth.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ReZherk.clinica.sistema.core.domain.entity.TokenSesion;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.TokenSesionRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordService {

 private final UsuarioRepository usuarioRepository;
 private final TokenSesionRepository tokenSesionRepository;
 private final PasswordEncoder passwordEncoder;
 private final EmailService emailService;

 public void enviarCorreoRecuperacion(String email) {

  Usuario usuario = usuarioRepository.findByEmail(email)
    .orElseThrow(() -> new RuntimeException("No existe un usuario con ese email"));

  String token = UUID.randomUUID().toString();

  TokenSesion tokenSesion = TokenSesion.builder()
    .usuario(usuario)
    .token(token)
    .fechaExpiracion(LocalDateTime.now().plusMinutes(30))
    .activo(true)
    .build();

  tokenSesionRepository.save(tokenSesion);

  emailService.enviarLinkRecuperacion(email, token);
 }

 public void resetearPassword(String token, String nuevaPassword) {

  TokenSesion t = tokenSesionRepository.findByTokenAndActivoTrue(token)
    .orElseThrow(() -> new RuntimeException("Token inválido"));

  if (t.getFechaExpiracion().isBefore(LocalDateTime.now())) {
   t.setActivo(false);
   tokenSesionRepository.save(t);
   throw new RuntimeException("El token ha expirado");
  }

  Usuario usuario = t.getUsuario();
  usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
  usuarioRepository.save(usuario);

  t.setActivo(false);
  tokenSesionRepository.save(t);
 }

}
