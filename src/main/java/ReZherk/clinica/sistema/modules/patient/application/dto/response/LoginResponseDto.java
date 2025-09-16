package ReZherk.clinica.sistema.modules.patient.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
 private Integer userId;
 private String nombres;
 private String apellidos;
 private String email;
 private String message;
}
