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
import ReZherk.clinica.sistema.infrastructure.security.JwtUtil;
import ReZherk.clinica.sistema.modules.auth.application.dto.request.LoginRequestDto;
import ReZherk.clinica.sistema.modules.auth.application.dto.response.LoginResponseDto;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UsuarioRepository usuarioRepository;
        private final JwtUtil jwtUtil;
        private final AuthenticationManager authenticationManager;

        @Transactional(readOnly = true)
        public LoginResponseDto login(LoginRequestDto loginDto) {
                Usuario usuario = usuarioRepository.findByNumeroDocumento(loginDto.getNumeroDocumento())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Usuario no encontrado con DNI: " + loginDto.getNumeroDocumento()));

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                loginDto.getNumeroDocumento(),
                                                loginDto.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                String jwt = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());

                // Obtener roles
                List<String> roles = usuario.getPerfiles().stream()
                                .map(rol -> rol.getNombre())
                                .collect(Collectors.toList());

                Set<String> permisos = usuario.getPerfiles().stream()
                                .flatMap(perfil -> perfil.getPermisos().stream())
                                .map(permiso -> permiso.getActionKey())
                                .distinct()
                                .collect(Collectors.toSet());

                // Respuesto
                return new LoginResponseDto(
                                true,
                                "Login exitoso",
                                usuario.getId(),
                                usuario.getNombres(),
                                usuario.getApellidos(),
                                usuario.getEmail(),
                                roles,
                                permisos,
                                jwt);

        }
}
