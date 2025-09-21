package ReZherk.clinica.sistema.modules.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponseDto {
 private Integer userId;
 private String nombres;
 private String apellidos;
 private String email;
 private String message; // Ejemplo: "Login exitoso"
 private List<String> roles; // Lista de roles asignados
 private String token;
}
