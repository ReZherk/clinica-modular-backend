package ReZherk.clinica.sistema.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  // Este método configura la cadena de seguridad de Spring Security.
  // Primero define las rutas públicas y protegidas, junto con las reglas de
  // acceso por rol.
  // Luego registra el filtro personalizado JwtAuthenticationFilter, indicando que
  // debe ejecutarse
  // antes del filtro estándar de autenticación por formulario.
  // En tiempo de ejecución, cuando llega una petición HTTP, el filtro JWT se
  // ejecuta primero,
  // valida el token y establece el contexto de seguridad con los roles del
  // usuario.
  // Después, Spring evalúa las reglas de autorización definidas para cada ruta,
  // verificando si el usuario tiene los permisos necesarios según su rol extraído
  // del JWT.
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // desactiva CSRF (útil en APIs REST)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html")
            .permitAll()
            .requestMatchers("/api/auth/**").permitAll() // login y auth en general
            .requestMatchers("/api/patient/register").permitAll() // registro de pacientes
            .requestMatchers("/api/admin/especialidad/register").hasAnyRole("ADMIN", "MEDICO") // registro de médicos
            // solo para admin
            .anyRequest().authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // sin sesiones
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .headers(headers -> headers.frameOptions(frame -> frame.disable())); // permite H2-console si la usas

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
