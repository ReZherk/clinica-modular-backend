package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponseDto {
 private Integer id;
 private String dni;
 private String nombreCompleto;
 private String rol;
 private Boolean estadoRegistro;
}
