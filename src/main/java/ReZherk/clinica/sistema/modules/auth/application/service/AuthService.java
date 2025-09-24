package ReZherk.clinica.sistema.modules.auth.application.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.infrastructure.security.JwtUtil;
import ReZherk.clinica.sistema.modules.auth.application.dto.request.LoginRequestDto;
import ReZherk.clinica.sistema.modules.auth.application.dto.response.LoginResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UsuarioRepository usuarioRepository;
        private final JwtUtil jwtUtil;
        private final AuthenticationManager authenticationManager;

        @Transactional(readOnly = true)
        public LoginResponseDto login(LoginRequestDto loginDto) {
                Usuario usuario = usuarioRepository.findByDni(loginDto.getDni())
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                "Usuario no encontrado con DNI: " + loginDto.getDni()));

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                loginDto.getDni(),
                                                loginDto.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                String jwt = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());

                // Obtener roles
                List<String> roles = usuario.getPerfiles().stream()
                                .map(rol -> rol.getNombre())
                                .collect(Collectors.toList());

                // Respuesto
                return new LoginResponseDto(
                                true,
                                "Login exitoso",
                                usuario.getId(),
                                usuario.getNombres(),
                                usuario.getApellidos(),
                                usuario.getEmail(),
                                roles,
                                jwt);

        }
}
