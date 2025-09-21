package ReZherk.clinica.sistema.modules.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.infrastructure.security.JwtUtil;
import ReZherk.clinica.sistema.modules.auth.application.dto.request.LoginRequestDto;
import ReZherk.clinica.sistema.modules.auth.application.dto.response.LoginResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public LoginResponseDto login(LoginRequestDto loginDto) {
    Usuario usuario = usuarioRepository.findByDni(loginDto.getDni())
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con DNI: " + loginDto.getDni()));

    // Validar contraseña con salt
    String rawPasswordWithSalt = loginDto.getPassword() + usuario.getSalt();
    if (!passwordEncoder.matches(rawPasswordWithSalt, usuario.getPasswordHash())) {
      throw new BusinessException("Credenciales inválidas");
    }

    // Obtener roles
    List<String> roles = usuario.getPerfiles()
        .stream()
        .map(rol -> rol.getNombre())
        .collect(Collectors.toList());

    // Generar token con roles
    String token = jwtUtil.generateToken(usuario.getDni(), roles.get(0));

    return new LoginResponseDto(
        usuario.getId(),
        usuario.getNombres(),
        usuario.getApellidos(),
        usuario.getEmail(),
        "Login exitoso",
        roles, token);
  }
}
