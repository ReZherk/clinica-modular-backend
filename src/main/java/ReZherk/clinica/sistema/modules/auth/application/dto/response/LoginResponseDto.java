package ReZherk.clinica.sistema.modules.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

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
 private Set<String> permisos;
 private String token;

 public LoginResponseDto(boolean success, String message) {
  this.success = success;
  this.message = message;
 }

}
