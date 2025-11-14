package ReZherk.clinica.sistema.modules.auth.application.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {
 private String token;
 private String nuevaPassword;
}