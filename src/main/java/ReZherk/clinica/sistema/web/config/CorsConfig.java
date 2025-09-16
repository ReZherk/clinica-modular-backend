package ReZherk.clinica.sistema.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

 @Bean
 public CorsConfigurationSource corsConfigurationSource() {
  CorsConfiguration configuration = new CorsConfiguration();

  // Permitir cualquier origen
  configuration.setAllowedOriginPatterns(Arrays.asList("*"));

  // Métodos HTTP permitidos
  configuration.setAllowedMethods(Arrays.asList(
    "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

  // Headers permitidos
  configuration.setAllowedHeaders(List.of("*"));

  // Permitir credenciales
  configuration.setAllowCredentials(true);

  // Asignar alas rutas sus configuraciones.
  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
  source.registerCorsConfiguration("/**", configuration);
  return source;
 }
}