package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
 private Integer id;
 private String numeroDocumento;
 private String tipoDocumento;
 private String nombreCompleto;
 private String email;
 private String telefono;
 private String rol;
 private Boolean estadoRegistro;

}
