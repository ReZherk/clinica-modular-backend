package ReZherk.clinica.sistema.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
 private Integer id;
 private String nombres;
 private String apellidos;
 private String email;
 private String telefono;
 private Boolean estadoRegistro;
}
