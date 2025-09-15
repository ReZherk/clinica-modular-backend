package ReZherk.clinica.sistema.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("API Sistema Clínica")
            .version("v1")
            .description("API REST para la gestión clínica (citas, usuarios, médicos, horarios)"));
  }
}