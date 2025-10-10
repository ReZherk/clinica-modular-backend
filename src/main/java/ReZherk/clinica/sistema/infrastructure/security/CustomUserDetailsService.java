package ReZherk.clinica.sistema.infrastructure.security;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * SERVICIO PERSONALIZADO PARA CARGAR DETALLES DEL USUARIO
 * 
 * Spring Security usa este servicio para:
 * 1. Autenticar usuarios (login)
 * 2. Cargar permisos/autoridades
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;

  /**
   * Carga usuario por DNI y construye UserDetails para Spring Security
   */
  @Override
  public UserDetails loadUserByUsername(String numeroDocumento) throws UsernameNotFoundException {
    // Buscar DNI en la base de datos
    Usuario user = usuarioRepository.findByNumeroDocumento(
        numeroDocumento)
        .orElseThrow(() -> new UsernameNotFoundException("Numero de documento no encontrado: " + numeroDocumento));

    // Construir objeto UserDetails que Spring Security entiende
    return new org.springframework.security.core.userdetails.User(
        user.getNumeroDocumento(), // dni de usuario
        user.getPasswordHash(), // Contraseña encriptada
        getAuthorities(user) // Permisos/autoridades del usuario
    );
  }

  /**
   * Obtiene todos los permisos del usuario (de todos sus roles)
   * 
   * EJEMPLO: Si usuario tiene rol ADMIN con permisos [USER_READ, USER_WRITE]
   * → Retorna [SimpleGrantedAuthority('USER_READ'),
   * SimpleGrantedAuthority('USER_WRITE')]
   */
  private Collection<? extends GrantedAuthority> getAuthorities(Usuario user) {
    return user.getPerfiles().stream()
        .flatMap(role -> role.getPermisos().stream()) // Aplanar todos los permisos de todos los roles
        .map(permission -> new SimpleGrantedAuthority(permission.getActionKey())) // Convertir a formato Spring Security
        .collect(Collectors.toSet()); // Eliminar duplicados
  }
}