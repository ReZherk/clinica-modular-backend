package ReZherk.clinica.sistema.modules.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

 @NotBlank
 private String dni;

 @NotBlank
 private String password;
}
