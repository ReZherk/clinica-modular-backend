package ReZherk.clinica.sistema.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder; // 👈 lo importas y se inyecta solo
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // habilita @PreAuthorize, @PostAuthorize, etc.
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthFilter;

  /*
   * private final PasswordEncoder passwordEncoder; // se inyecta desde
   * PasswordConfig
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Bean para manejar autenticación.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * Configuración de seguridad HTTP.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // CSRF no es necesario en APIs REST
        .csrf(csrf -> csrf.disable())
        // Configuración de autorización
        .authorizeHttpRequests(auth -> auth
            // Endpoints públicos
            .requestMatchers(
                "/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/api/auth/**", // login, refresh, etc.
                "/api/patient/register" // registro de pacientes
            ).permitAll()

            // Endpoints restringidos
            .requestMatchers("/api/admin/**").hasAuthority("ROLE_READ")
            .requestMatchers("/api/medico/**").hasAuthority("ADMIN_ACCESS")

            // Todo lo demás requiere autenticación
            .anyRequest().authenticated())
        // Sin estado (JWT)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Registro del filtro JWT
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        // Opcional: permitir iframes del mismo origen (ej. consola H2)
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

    return http.build();
  }
}
