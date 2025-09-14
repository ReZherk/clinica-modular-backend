package ReZherk.clinica.sistema.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDto {
 private Integer id;
 private String message;
 private UsuarioResponseDto usuario;
}
