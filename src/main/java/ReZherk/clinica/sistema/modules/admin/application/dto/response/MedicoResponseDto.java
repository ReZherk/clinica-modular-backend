package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoResponseDto {
 private Integer id;
 private String nombres;
 private String apellidos;
 private String email;
 private String telefono;
 private boolean estadoRegistro;
 private String cmp;
}
