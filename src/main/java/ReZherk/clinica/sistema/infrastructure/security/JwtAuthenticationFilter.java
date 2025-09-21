package ReZherk.clinica.sistema.infrastructure.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    // Validar que el header exista y tenga el prefijo "Bearer "

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);

      if (jwtUtil.validateToken(token)) {
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        // Convertir el rol en una autoridad de Spring Security
        List<SimpleGrantedAuthority> authorities = Collections
            .singletonList(new SimpleGrantedAuthority("ROLE_" + role));

        // Crear el objeto de autenticación con el usuario y sus roles
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            new User(username, "", authorities),
            null,
            authorities);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Registrar el token de autenticación en el contexto de seguridad
        // Esto permite que Spring Security evalúe reglas como hasAnyRole(...)
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}
