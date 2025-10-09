package ReZherk.clinica.sistema.modules.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

 @NotBlank
 private String numeroDocumento;

 @NotBlank
 private String password;
}
