package ReZherk.clinica.sistema.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // desactiva CSRF (útil en APIs REST)
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll() // ⚠️ todas las rutas son públicas por ahora
        )
        .headers(headers -> headers.frameOptions(frame -> frame.disable())); // permite H2-console si la usas

    return http.build();
  }
}
