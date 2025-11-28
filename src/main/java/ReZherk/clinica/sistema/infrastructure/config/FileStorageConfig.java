package ReZherk.clinica.sistema.infrastructure.config;

import org.springframework.context.annotation.Configuration;

// ResourceHandlerRegistry: permite registrar rutas para archivos estáticos
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

// WebMvcConfigurer: interfaz para personalizar la configuración de Spring MVC
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.lang.NonNull;

@Configuration
public class FileStorageConfig implements WebMvcConfigurer {

 @Override
 public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {

  // Expone públicamente las imágenes que estén en el directorio local:
  // uploads/fotos-perfil/

  // Esto significa:
  // Cuando alguien acceda a:
  // /api/files/fotos-perfil/archivo.jpg
  // Spring irá a buscar el archivo en:
  // uploads/fotos-perfil/archivo.jpg

  registry
    // URL pública para acceder a los archivos
    .addResourceHandler("/api/files/fotos-perfil/**")

    // Ruta local donde realmente se guardan las imágenes
    // "file:" = indica que es una ruta del sistema de archivos
    .addResourceLocations("file:uploads/fotos-perfil/");
 }

}