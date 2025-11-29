package ReZherk.clinica.sistema.modules.patient.web.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public class FotoUploadSchema {

 @Schema(type = "string", format = "binary", description = "Archivo de la foto de perfil")
 public MultipartFile foto;
}
