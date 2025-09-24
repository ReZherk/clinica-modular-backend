package ReZherk.clinica.sistema.infrastructure.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * FILTRO JWT: Se ejecuta en cada petición para validar tokens
 * 
 * FUNCIONAMIENTO:
 * 1. Cliente envía petición con header: Authorization: Bearer {token}
 * 2. Este filtro intercepta la petición
 * 3. Extrae y valida el token JWT
 * 4. Si es válido, establece la autenticación en Spring Security
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  private final CustomUserDetailsService userDetailsService;

  /**
   * FILTRO PRINCIPAL: Se ejecuta en cada petición
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    // Extraer header Authorization
    final String authorizationHeader = request.getHeader("Authorization");

    String dni = null;
    String jwt = null;

    // Verificar si el header existe y comienza con "Bearer "
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7); // Extraer token (remover "Bearer ")
      dni = jwtUtil.getDniFromToken(jwt); // Extraer username del token
    }

    // Si tenemos username y no hay autenticación actual...
    if (dni != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      // Cargar detalles del usuario desde la base de datos
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(dni);

      // Validar token JWT
      if (jwtUtil.validateToken(jwt, userDetails)) {
        // Crear objeto de autenticación para Spring Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null, // Credenciales (null porque JWT ya las validó)
            userDetails.getAuthorities() // Permisos del usuario
        );

        // Agregar detalles de la petición web
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Establecer autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }

    // Continuar con la cadena de filtros
    chain.doFilter(request, response);
  }
}