package ReZherk.clinica.sistema.modules.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponseDto {

 private boolean success;
 private String message;

 private Integer userId;
 private String nombres;
 private String apellidos;
 private String email;
 private List<String> roles; // Lista de roles asignados
 private String token;

 public LoginResponseDto(boolean success, String message) {
  this.success = success;
  this.message = message;
 }

}
