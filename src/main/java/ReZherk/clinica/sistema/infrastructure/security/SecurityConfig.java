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
import org.springframework.security.crypto.password.PasswordEncoder;
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
            .requestMatchers(
                "/api/admin/especialidad/all",
                "/api/admin/especialidad/all/with-medicos",
                "/api/admin/especialidad/*/medicos",
                "/api/admin/roles/permissions",
                "/api/admin/roles",
                "/api/admins")
            .hasAuthority("GLOBAL_READ")
            .requestMatchers(
                "/api/admin/especialidad/create",
                "/api/admin/especialidad/*/activar",
                "/api/admin/especialidad/*/desactivar",
                "/api/admin/especialidad/*")
            .hasAuthority("SPECIALITY_MANAGE")

            .requestMatchers(
                "/api/admin/roles/create",
                "/api/admin/roles/{id}",
                "/api/admin/roles/{id}/activate",
                "/api/admin/roles/{id}/deactivate")
            .hasAuthority("ROLE_MANAGE")

            .requestMatchers(
                "/api/admin/roles/assign")
            .hasAuthority("ROLE_ASSIGN")

            .requestMatchers(
                "/api/admin/roles/create-admin",
                "/api/admins/{id}",
                "/api/admins/{id}/activate",
                "/api/admins/{id}/deactivate")
            .hasAuthority("SUPER_ADMIN")

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
