package ReZherk.clinica.sistema.modules.patient.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubirFotoPerfilRequestDto {

 @NotNull(message = "El ID del usuario es obligatorio")
 @Positive(message = "El ID del usuario debe ser positivo")
 private Integer idUsuario;

 @NotNull(message = "La foto es obligatoria")
 private MultipartFile foto;
}
