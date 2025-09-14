package ReZherk.clinica.sistema.dto;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDto {
 private Integer id;
 private String nombres;
 private String apellidos;
 private String email;
 private String telefono;
 private Boolean estadoRegistro;
 private Set<String> roles;
}